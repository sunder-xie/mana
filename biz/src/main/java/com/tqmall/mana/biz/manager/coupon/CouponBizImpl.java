package com.tqmall.mana.biz.manager.coupon;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.coupon.CouponSendLogBO;
import com.tqmall.mana.beans.BO.coupon.SearchCouponSendLogBO;
import com.tqmall.mana.beans.BO.coupon.SendCouponBO;
import com.tqmall.mana.beans.BO.coupon.UpdateCouponBO;
import com.tqmall.mana.beans.entity.coupon.ManaCouponDO;
import com.tqmall.mana.beans.entity.coupon.ManaCouponSendLogDO;
import com.tqmall.mana.beans.entity.coupon.ManaCouponTypeDO;
import com.tqmall.mana.beans.entity.sms.ManaSmsTemplateDO;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.biz.manager.sms.SmsBiz;
import com.tqmall.mana.component.annotation.cache.ManaCache;
import com.tqmall.mana.component.bean.ConstantBean;
import com.tqmall.mana.component.enums.CouponStatusEnum;
import com.tqmall.mana.component.redis.RedisKeyBean;
import com.tqmall.mana.component.util.DateUtil;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.component.util.excel.PoiUtil;
import com.tqmall.mana.component.util.mana.ManaUtil;
import com.tqmall.mana.dao.mapper.coupon.ManaCouponDOMapper;
import com.tqmall.mana.dao.mapper.coupon.ManaCouponSendLogDOMapper;
import com.tqmall.mana.dao.mapper.coupon.ManaCouponTypeDOMapper;
import com.tqmall.mana.external.dubbo.stall.ExtSmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by huangzhangting on 16/12/6.
 */
@Slf4j
@Service
public class CouponBizImpl implements CouponBiz {
    private static final Lock SEND_COUPON_LOCK = new ReentrantLock(true);

    @Autowired
    private ExtSmsService extSmsService;
    @Autowired
    private ManaCouponDOMapper couponDOMapper;
    @Autowired
    private ManaCouponSendLogDOMapper couponSendLogDOMapper;
    @Autowired
    private ManaCouponTypeDOMapper couponTypeDOMapper;
    @Autowired
    private SmsBiz smsBiz;
    @Autowired
    private ShiroBiz shiroBiz;


    private String checkSendCoupon(SendCouponBO sendCouponBO){
        if(sendCouponBO==null){
            return "参数不能为空";
        }
        if(StringUtils.isEmpty(sendCouponBO.getSmsTemplateKey())){
            return "短信模板不能为空";
        }
        if(sendCouponBO.getCouponType()==null || sendCouponBO.getCouponType()<1){
            return "券类型不能为空";
        }
        if(sendCouponBO.getCouponNum()==null || sendCouponBO.getCouponNum()<1){
            return "发送数量不能小于1";
        }
        if(StringUtils.isEmpty(sendCouponBO.getMobiles())){
            return "手机号不能为空";
        }

        return null;
    }
    private boolean checkMobiles(String[] mobileArray){
        if(mobileArray==null){
            return false;
        }
        for(String mobile : mobileArray){
            if(!ManaUtil.isMobile(mobile)){
                return false;
            }
        }
        return true;
    }

    private boolean sendMsg(String mobile, String content, String action){
//        String action = "mana_movie_key";
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("code", content);
        Result result = extSmsService.sendMsg(mobile, action, dataMap);
        return result.isSuccess();
    }
    private Map<String, Object> getMsgContent(List<ManaCouponDO> couponDOList, int num){
        StringBuilder sb = new StringBuilder();
        List<Integer> idList = new ArrayList<>();
        for(int i=0; i<num; i++){
            ManaCouponDO couponDO = couponDOList.remove(0);
            sb.append("、");
            if(couponDO.getIsEncrypt()==1){ //如果加密过
                String str = ManaUtil.aesDecrypt(couponDO.getCouponContent(), ConstantBean.ENCRYPT_KEY);
                if(str==null){
                    sb.append(couponDO.getCouponContent());
                }else{
                    sb.append(str);
                }
            }else {
                sb.append(couponDO.getCouponContent());
            }
            idList.add(couponDO.getId());
        }
        sb.deleteCharAt(0);

        Map<String, Object> map = new HashMap<>();
        map.put("msg", sb.toString());
        map.put("idList", idList);

        return map;
    }
    @Override
    public Result sendCoupon(SendCouponBO sendCouponBO) {
        String checkStr = checkSendCoupon(sendCouponBO);
        if(checkStr != null){
            log.info("send coupon failed, param:{}", JsonUtil.objectToStr(sendCouponBO));
            return ResultUtil.errorResult("", checkStr);
        }
        String mobiles = sendCouponBO.getMobiles().replace("，", ",").replace(" ", ""); //去掉中文逗号，空格
        String[] mobileArray = mobiles.split(",");
        if(!checkMobiles(mobileArray)){
            return ResultUtil.errorResult("", "存在错误的手机号，请再次检查");
        }
        int mobileLen = mobileArray.length; //手机号数量
        int couponNum = sendCouponBO.getCouponNum();
        int limitNum = mobileLen * couponNum; //待发送数量

        List<ManaCouponSendLogDO> sendLogDOList = new ArrayList<>();
        String operator = shiroBiz.getCurrentUserRealName();

        SEND_COUPON_LOCK.lock();

        try{
            List<ManaCouponDO> couponDOList = couponDOMapper.selectAvailableList(sendCouponBO.getCouponType(), limitNum);
            int size = couponDOList.size();
            if(size < limitNum){
                return ResultUtil.errorResult("", "优惠券不足，剩余可用数量："+size+"，需要发送数量："+limitNum);
            }

            for(int i=0; i<mobileLen; i++){
                Map<String, Object> msgMap = getMsgContent(couponDOList, couponNum);
                List<Integer> idList = (List)msgMap.get("idList");
                String msg = msgMap.get("msg").toString();

                UpdateCouponBO updateCouponBO = new UpdateCouponBO();
                updateCouponBO.setModifier(operator);
                updateCouponBO.setGmtModified(new Date());
                updateCouponBO.setIdList(idList);

                String mobile = mobileArray[i];
                if(sendMsg(mobile, msg, sendCouponBO.getSmsTemplateKey())){
                    log.info("优惠券发送成功, idList:{}", idList.toString());
                    updateCouponBO.setCouponStatus(CouponStatusEnum.SEND_SUCCESS.getCode());
                }else{
                    log.info("优惠券发送失败, idList:{}", idList.toString());
                    updateCouponBO.setCouponStatus(CouponStatusEnum.SEND_FAILED.getCode());
                }

                /** 更新优惠券状态 */
                couponDOMapper.updateCouponStatus(updateCouponBO);

                ManaCouponSendLogDO sendLogDO = new ManaCouponSendLogDO();
                sendLogDO.setCreator(operator);
                sendLogDO.setGmtCreate(updateCouponBO.getGmtModified());
                sendLogDO.setSendDate(DateUtil.dateToString(sendLogDO.getGmtCreate(), DateUtil.yyyy_MM_dd));

                //内容加密
                String str = ManaUtil.aesEncrypt(msg, ConstantBean.ENCRYPT_KEY);
                if(str==null){
                    sendLogDO.setSendContent(msg);
                    sendLogDO.setIsEncrypt(0);
                }else{
                    sendLogDO.setSendContent(str);
                    sendLogDO.setIsEncrypt(1);
                }

                sendLogDO.setSendStatus(updateCouponBO.getCouponStatus());
                sendLogDO.setSendMobile(mobile);
                sendLogDO.setCouponNum(couponNum);
                sendLogDO.setCouponTypeId(sendCouponBO.getCouponType());
                sendLogDO.setTemplateKey(sendCouponBO.getSmsTemplateKey());

                sendLogDOList.add(sendLogDO);
            }

        }catch (Exception e){
            log.error("send coupon error", e);
        }finally {
            SEND_COUPON_LOCK.unlock();
        }

        /** 批量插入发送记录 */
        log.info("批量插入发送记录, dataList:{}", JsonUtil.objectToStr(sendLogDOList));
        if(!sendLogDOList.isEmpty()) {
            couponSendLogDOMapper.batchInsert(sendLogDOList);
        }

        return ResultUtil.successResult(limitNum);
    }

    @ManaCache(cacheName = RedisKeyBean.COUPON_TYPE_KEY)
    @Override
    public List<ManaCouponTypeDO> getAllTypes() {
        List<ManaCouponTypeDO> list = couponTypeDOMapper.selectAll();
        if(list.isEmpty()){
            log.info("没有配置优惠券类型");
        }
        return list;
    }

    @Override
    public PagingResult<ManaCouponSendLogDO> getSendLogPage(SearchCouponSendLogBO searchCouponSendLogBO) {
        if(searchCouponSendLogBO==null){
            return PagingResult.wrapErrorResult("", "获取列表数据失败，查询参数不能为空");
        }
        Integer pageIndex = searchCouponSendLogBO.getPageIndex();
        if(pageIndex==null || pageIndex<1){
            pageIndex = 1;
        }
        Integer pageSize = searchCouponSendLogBO.getPageSize();
        if(pageSize==null || pageSize<1){
            pageSize = 10;
            searchCouponSendLogBO.setPageSize(pageSize);
        }
        int offset = (pageIndex-1)*pageSize;
        searchCouponSendLogBO.setOffset(offset);

        List<ManaCouponSendLogDO> logDOList = couponSendLogDOMapper.selectPage(searchCouponSendLogBO);
        if(logDOList.isEmpty()){
            return PagingResult.wrapErrorResult("", "暂无数据");
        }

        //查询短信模板
        List<ManaSmsTemplateDO> smsTemplateDOList = smsBiz.getAllTemplate();

        for(ManaCouponSendLogDO sendLogDO : logDOList){
            sendLogDO.setGmtCreateStr(DateUtil.dateToString(sendLogDO.getGmtCreate(), DateUtil.yyyy_MM_dd_HH_mm_ss));
            if(sendLogDO.getIsEncrypt()==1){
                String str = ManaUtil.aesDecrypt(sendLogDO.getSendContent(), ConstantBean.ENCRYPT_KEY);
                if(str!=null){
                    sendLogDO.setSendContent(str);
                }
            }
            setSmsTemplateInfo(sendLogDO, smsTemplateDOList);
        }
        int count = couponSendLogDOMapper.selectCount(searchCouponSendLogBO);

        return PagingResult.wrapSuccessfulResult(logDOList, count);
    }
    /** 设置短信模板相关信息 */
    private void setSmsTemplateInfo(ManaCouponSendLogDO sendLogDO, List<ManaSmsTemplateDO> smsTemplateDOList){
        ManaSmsTemplateDO templateDO = getSmsTemplate(sendLogDO.getTemplateKey(), smsTemplateDOList);
        if(templateDO!=null){
            sendLogDO.setTemplateKey(templateDO.getTemplateDesc()); //字段复用，给前端展示
            sendLogDO.setTemplateContent(templateDO.getTemplateContent());
        }
    }
    private ManaSmsTemplateDO getSmsTemplate(String templateKey, List<ManaSmsTemplateDO> smsTemplateDOList){
        if(smsTemplateDOList.isEmpty() || "".equals(templateKey)){
            return null;
        }
        for(ManaSmsTemplateDO templateDO : smsTemplateDOList){
            if(templateKey.equals(templateDO.getTemplateKey())){
                return templateDO;
            }
        }
        return null;
    }

    @Override
    public void exportSendLog(HttpServletResponse response, SearchCouponSendLogBO searchCouponSendLogBO) {
        PoiUtil poiUtil = new PoiUtil();
        if(searchCouponSendLogBO==null){
            searchCouponSendLogBO = new SearchCouponSendLogBO();
        }
        int count = couponSendLogDOMapper.selectCount(searchCouponSendLogBO);
        if(count==0){
            try {
                poiUtil.noDataExcel(response);
            } catch (Exception e) {
                log.error("export send log excel error", e);
            }
            return;
        }
        int pageSize = 500;
        searchCouponSendLogBO.setPageSize(500);
        searchCouponSendLogBO.setOffset(0);
        List<ManaCouponSendLogDO> logDOList = couponSendLogDOMapper.selectPage(searchCouponSendLogBO);
        if(count>pageSize){
            int pages = count%pageSize==0?count/pageSize:(count/pageSize+1);
            for(int i=1; i<pages; i++){
                int offset = pages * pageSize;
                searchCouponSendLogBO.setOffset(offset);
                logDOList.addAll(couponSendLogDOMapper.selectPage(searchCouponSendLogBO));
            }
        }

        List<CouponSendLogBO> sendLogBOList = convertToLogBOList(logDOList);

        String[] heads = new String[]{"发送手机号", "券类型", "数量", "发送内容", "发送日期", "状态", "短信模板"};
        String[] fields = new String[]{"sendMobile", "couponTypeName", "couponNum", "sendContent", "gmtCreateStr",
                "sendStatusStr", "templateDesc"};

        try {
            poiUtil.exportXlsxToClient(response, "优惠券发送列表", heads, fields, sendLogBOList);
        } catch (Exception e) {
            log.error("export send log excel error", e);
        }
    }

    private List<CouponSendLogBO> convertToLogBOList(List<ManaCouponSendLogDO> logDOList){
        //组装类型
        List<ManaCouponTypeDO> typeDOList = getAllTypes();
        Map<Integer, String> typeMap = new HashMap<>();
        for(ManaCouponTypeDO typeDO : typeDOList){
            typeMap.put(typeDO.getId(), typeDO.getTypeName());
        }
        //查询短信模板
        List<ManaSmsTemplateDO> smsTemplateDOList = smsBiz.getAllTemplate();

        List<CouponSendLogBO> sendLogBOList = new ArrayList<>();
        for(ManaCouponSendLogDO sendLogDO : logDOList){
            CouponSendLogBO couponSendLogBO = new CouponSendLogBO();
            couponSendLogBO.setId(sendLogDO.getId());
            couponSendLogBO.setSendMobile(sendLogDO.getSendMobile());
            couponSendLogBO.setCouponNum(sendLogDO.getCouponNum());
            couponSendLogBO.setGmtCreateStr(DateUtil.dateToString(sendLogDO.getGmtCreate(), DateUtil.yyyy_MM_dd_HH_mm_ss));
            //设置状态
            switch (sendLogDO.getSendStatus()){
                case 1: couponSendLogBO.setSendStatusStr("发送成功"); break;
                case 2: couponSendLogBO.setSendStatusStr("发送失败"); break;
                default: couponSendLogBO.setSendStatusStr(sendLogDO.getSendStatus()+""); break;
            }
            //设置类型
            String typeName = typeMap.get(sendLogDO.getCouponTypeId());
            if(typeName==null){
                couponSendLogBO.setCouponTypeName(sendLogDO.getCouponTypeId()+"");
            }else{
                couponSendLogBO.setCouponTypeName(typeName);
            }
            //设置内容
            if(sendLogDO.getIsEncrypt()==1){
                couponSendLogBO.setSendContent(ManaUtil.aesDecrypt(sendLogDO.getSendContent(), ConstantBean.ENCRYPT_KEY));
            }else{
                couponSendLogBO.setSendContent(sendLogDO.getSendContent());
            }
            //设置模板
            ManaSmsTemplateDO templateDO = getSmsTemplate(sendLogDO.getTemplateKey(), smsTemplateDOList);
            if(templateDO!=null){
                couponSendLogBO.setTemplateDesc(templateDO.getTemplateDesc());
            }

            sendLogBOList.add(couponSendLogBO);
        }

        return sendLogBOList;
    }

}
