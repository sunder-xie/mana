package com.tqmall.mana.biz.manager.settle.config;

import com.google.common.collect.Lists;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.mana.beans.BO.settle.CheckSettleApplyRangeConfigBO;
import com.tqmall.mana.beans.BO.settle.DeleteRegionConfigBO;
import com.tqmall.mana.beans.BO.settle.DeleteShopRuleItemBO;
import com.tqmall.mana.beans.BO.settle.SettleRateBO;
import com.tqmall.mana.beans.VO.settle.*;
import com.tqmall.mana.beans.VO.settle.shopRule.*;
import com.tqmall.mana.beans.VO.settle.shopRule.add.AddSettleShopRuleItemVO;
import com.tqmall.mana.beans.VO.settle.shopRule.add.AddSettleShopRuleVO;
import com.tqmall.mana.beans.entity.settle.*;
import com.tqmall.mana.beans.entity.settle.extend.SettleConfigItemExtendDO;
import com.tqmall.mana.beans.param.settle.SettleShopRulePageParam;
import com.tqmall.mana.beans.param.settle.ShopSettleRateQueryParam;
import com.tqmall.mana.biz.manager.insurance.InsuranceDicBiz;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.component.enums.insurance.dict.*;
import com.tqmall.mana.component.exception.BusinessCheckException;
import com.tqmall.mana.component.exception.BusinessException;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.dao.mapper.settle.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 *
 * 门店结算规则配置业务代码
 *
 * Created by huangzhangting on 17/1/13.
 */
@Slf4j
@Service
public class SettleShopRuleBizImpl implements SettleShopRuleBiz {
    @Autowired
    private ShiroBiz shiroBiz;
    @Autowired
    private SettleShopRuleDOMapper shopRuleDOMapper;
    @Autowired
    private SettleShopRuleItemDOMapper shopRuleItemDOMapper;
    @Autowired
    private SettleShopRuleShopConfigDOMapper shopConfigDOMapper;
    @Autowired
    private SettleShopRuleRegionConfigDOMapper regionConfigDOMapper;
    @Autowired
    private SettleConfigBasicDOMapper configBasicDOMapper;
    @Autowired
    private SettleConfigItemDOMapper configItemDOMapper;
    @Autowired
    private InsuranceDicBiz insuranceDicBiz;


    private void throwBizException(String msg){
        throw new BusinessException(msg);
    }

    /** 新增规则参数校验 */
    private void checkRegionConfig(AddSettleShopRuleVO shopRuleVO){
        List<SettleShopRuleRegionConfigVO> regionConfigVOList = shopRuleVO.getRegionConfigVOList();
        Assert.notEmpty(regionConfigVOList, "请选择需要配置的地区");
        List<String> cityCodeList = new ArrayList<>();
        for(SettleShopRuleRegionConfigVO configVO : regionConfigVOList){
            String cityCode = configVO.getCityCode();
            if(StringUtils.isEmpty(cityCode) || StringUtils.isEmpty(configVO.getProvinceCode())){
                throwBizException("配置的地区，缺少必要编码，请联系技术人员");
            }
            cityCodeList.add(cityCode);
        }
        //检查地区是否已经配置过结算规则
        CheckSettleApplyRangeConfigBO configBO = BdUtil.do2bo(shopRuleVO, CheckSettleApplyRangeConfigBO.class);
        configBO.setCityCodeList(cityCodeList);
        List<String> cityList = regionConfigDOMapper.checkRegionConfig(configBO);
        if(!cityList.isEmpty()){
            throwBizException("所选地区已经配置过结算规则，不能重复添加，重复配置的地区："+cityList.toString());
        }
    }
    private void checkShopConfig(AddSettleShopRuleVO shopRuleVO){
        SettleShopRuleShopConfigVO shopConfigVO = shopRuleVO.getShopConfigVO();
        if(shopConfigVO==null || shopConfigVO.getShopId()==null
                || StringUtils.isEmpty(shopConfigVO.getShopAccount())
                || StringUtils.isEmpty(shopConfigVO.getShopName())){

            throwBizException("请选择需要配置的门店");
        }
        //检查门店是否已经配置过结算规则
        CheckSettleApplyRangeConfigBO configBO = BdUtil.do2bo(shopRuleVO, CheckSettleApplyRangeConfigBO.class);
        configBO.setShopId(shopConfigVO.getShopId());
        Integer id = shopConfigDOMapper.checkShopConfig(configBO);
        Assert.isNull(id, "所选门店已经配置过结算规则，不能重复添加");
    }
    private void checkApplyRange(AddSettleShopRuleVO shopRuleVO){
        Integer applyRange = shopRuleVO.getApplyRange();
        Assert.notNull(applyRange, "请选择适用范围");

        if(ApplyRangeEnum.REGION.getCode().equals(applyRange)){
            checkRegionConfig(shopRuleVO);
        }else if(ApplyRangeEnum.SHOP.getCode().equals(applyRange)){
            checkShopConfig(shopRuleVO);
        }else{
            throwBizException("无法识别的适用范围："+applyRange);
        }
    }

    /** 规则条目，细粒度校验 */
    //按结束时间排序（升序）
    private void sortRuleItem(List<AddSettleShopRuleItemVO> ruleItemVOList){
        Collections.sort(ruleItemVOList, new Comparator<AddSettleShopRuleItemVO>() {
            @Override
            public int compare(AddSettleShopRuleItemVO o1, AddSettleShopRuleItemVO o2) {
                Date endTime1 = o1.getEndTime();
                Date endTime2 = o2.getEndTime();
                if(endTime1==null){
                    if(endTime2==null){
                        throwBizException("请选择正确的生效起止日期");
                    }
                    return 1;
                }
                if(endTime2==null){
                    return -1;
                }
                int flag = endTime1.compareTo(endTime2);
                if(flag==0){
                    throwBizException("请选择正确的生效起止日期，多组配置的结束时间不能相等");
                }
                return flag;
            }
        });
    }
    private void checkRuleItemList(List<AddSettleShopRuleItemVO> ruleItemVOList, Set<Integer> basicIdSet, boolean forReward){
        if(CollectionUtils.isEmpty(ruleItemVOList)){
            return;
        }
        int size = ruleItemVOList.size();
        if(size == 0){
            AddSettleShopRuleItemVO itemVO = ruleItemVOList.get(0);
            checkRuleItem(itemVO, forReward);
            checkItemEndTime(itemVO);
            itemVO.setStartTime(handleStartDate(new Date()));
            //itemVO.setEndTime(handleEndDate(itemVO.getEndTime()));
            basicIdSet.add(itemVO.getSettleConfigBasicId());
            return;
        }

        //按结束时间排序（升序）
        sortRuleItem(ruleItemVOList);

        //最后一条数据结束时间必须为null
        AddSettleShopRuleItemVO itemVO = ruleItemVOList.get(size-1);
        checkItemEndTime(itemVO);

        //处理第一条数据
        itemVO = ruleItemVOList.get(0);
        checkRuleItem(itemVO, forReward);
        itemVO.setStartTime(handleStartDate(new Date()));
        //无需再次判断，在排序的过程中已经满足验证了
        //checkItemStartEndTime(itemVO);
        itemVO.setEndTime(handleEndDate(itemVO.getEndTime()));
        basicIdSet.add(itemVO.getSettleConfigBasicId());

        /* 处理剩余数据 */
        for(int i=1; i<size; i++){
            itemVO = ruleItemVOList.get(i);
            checkRuleItem(itemVO, forReward);
            //设置开始日期
            AddSettleShopRuleItemVO preItemVO = ruleItemVOList.get(i-1);
            itemVO.setStartTime(addOneDay(preItemVO.getEndTime()));
            //checkItemStartEndTime(itemVO);
            itemVO.setEndTime(handleEndDate(itemVO.getEndTime()));
            basicIdSet.add(itemVO.getSettleConfigBasicId());
        }
    }
    private Date addOneDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return handleStartDate(calendar.getTime());
    }
    private Date handleStartDate(Date date){
        if(date==null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    private Date handleEndDate(Date date){
        if(date==null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    private void checkRuleItem(AddSettleShopRuleItemVO ruleItem, boolean forReward){
        checkItemForReward(ruleItem);
        if (!forReward) {
            checkItemForPackage(ruleItem);
        }
    }
    //服务包规则参数校验
    private void checkItemForPackage(AddSettleShopRuleItemVO ruleItem){
        Assert.notNull(ruleItem.getSettleCondition(), "请选择结算条件");
        Assert.notNull(ruleItem.getFundType(), "请选择资金类型");
        Assert.notNull(ruleItem.getRebateType(), "请选择返点类型");
//        Assert.notNull(ruleItem.getEndTime(), "请选择生效起止日期");
    }
    private void checkItemEndTime(AddSettleShopRuleItemVO itemVO){
        //只有一条明细时，最大时间默认为null
        if(itemVO.getEndTime() != null){
            throwBizException("请选择正确的生效起止日期");
        }
    }
    /* 校验起止日期 */
    private void checkItemStartEndTime(AddSettleShopRuleItemVO itemVO){
        Date startTime = itemVO.getStartTime();
        Date endTime = itemVO.getEndTime();
        if(endTime != null){
            if(startTime.compareTo(endTime) > 0){
                throwBizException("请选择正确的生效起止日期，结束日期不能小于开始日期");
            }
        }
    }
    private void checkGroupIds(Set<Integer> basicIdSet){
        if(basicIdSet.isEmpty()){
            return;
        }
        List<String> deletedGroups = configBasicDOMapper.getDeletedGroupByIds(basicIdSet);
        if(!deletedGroups.isEmpty()){
            throwBizException("所选的部分保费区间分组已被删除，请重新选择分组，被删除分组："+deletedGroups.toString());
        }
    }

    private void checkAddShopRule(AddSettleShopRuleVO shopRuleVO){
        Assert.notNull(shopRuleVO, "参数不能为空");
        Assert.notNull(shopRuleVO.getCooperationMode(), "缺少必要参数：合作模式");
        Assert.notNull(shopRuleVO.getInsuranceCompanyId(), "请选择保险公司");

        if(shopRuleVO.getPackageRuleItem()==null && CollectionUtils.isEmpty(shopRuleVO.getBizRuleItemList())
                && CollectionUtils.isEmpty(shopRuleVO.getForceRuleItemList())){
            throwBizException("至少选择一种配置基准");
        }
        checkApplyRange(shopRuleVO);
        Set<Integer> basicIdSet = new HashSet<>();
        checkRuleItemList(shopRuleVO.getBizRuleItemList(), basicIdSet, false);
        checkRuleItemList(shopRuleVO.getForceRuleItemList(), basicIdSet, false);
        //检验保费区间组是否合法
        checkGroupIds(basicIdSet);

        AddSettleShopRuleItemVO ruleItemVO = shopRuleVO.getPackageRuleItem();
        if(ruleItemVO != null){
            checkItemForPackage(ruleItemVO);
            checkItemEndTime(ruleItemVO);
            ruleItemVO.setStartTime(handleStartDate(new Date()));
            ruleItemVO.setCalculateMode(CalculateModeEnum.SINGLE.getCode());
            ruleItemVO.setSettleConfigBasicId(0);
        }

    }

    @Transactional
    @Override
    public boolean addSettleShopRule(AddSettleShopRuleVO shopRuleVO) {
        /** 入参校验 */
        checkAddShopRule(shopRuleVO);

        /** 插入主表数据 */
        SettleShopRuleDO shopRuleDO = saveShopRule(shopRuleVO);

        /** 处理，保存适用范围 */
        saveApplyRange(shopRuleVO, shopRuleDO);

        /** 组装规则明细，插入数据 */
        List<SettleShopRuleItemDO> ruleItemDOList = new ArrayList<>();
        packageRuleItem(ruleItemDOList, shopRuleVO.getBizRuleItemList(), RebateStandardEnum.BIZ_INSURANCE.getCode(), shopRuleDO);
        packageRuleItem(ruleItemDOList, shopRuleVO.getForceRuleItemList(), RebateStandardEnum.FORCE_INSURANCE.getCode(), shopRuleDO);
        // 处理服务包规则
        AddSettleShopRuleItemVO shopRuleItemVO = shopRuleVO.getPackageRuleItem();
        if(shopRuleItemVO != null) {
            SettleShopRuleItemDO itemDO = convertRuleItemVO(shopRuleItemVO, RebateStandardEnum.PACKAGE.getCode(), shopRuleDO);
            ruleItemDOList.add(itemDO);
        }
        shopRuleItemDOMapper.batchInsert(ruleItemDOList);

        return true;
    }
    /** 保存规则主表数据 */
    private SettleShopRuleDO saveShopRule(AddSettleShopRuleVO shopRuleVO){
        SettleShopRuleDO shopRuleDO = BdUtil.do2bo(shopRuleVO, SettleShopRuleDO.class);
        shopRuleDO.setId(null);
        shopRuleDO.setGmtCreate(new Date());
        shopRuleDO.setCreator(shiroBiz.getCurrentUserRealName());

        shopRuleDOMapper.insertSelective(shopRuleDO);
        Integer id = shopRuleDO.getId();
        if(id == null){
            throwBizException("比例配置失败");
        }

        return shopRuleDO;
    }
    /** 处理保存适用范围 */
    private void saveApplyRange(AddSettleShopRuleVO shopRuleVO, SettleShopRuleDO ruleDO){
        /** 插入门店信息 */
        SettleShopRuleShopConfigVO shopConfigVO = shopRuleVO.getShopConfigVO();
        if(shopConfigVO != null){
            SettleShopRuleShopConfigDO shopConfigDO = BdUtil.do2bo(shopConfigVO, SettleShopRuleShopConfigDO.class);
            shopConfigDO.setGmtCreate(ruleDO.getGmtCreate());
            shopConfigDO.setCreator(ruleDO.getCreator());
            shopConfigDO.setSettleRuleId(ruleDO.getId());
            shopConfigDOMapper.insertSelective(shopConfigDO);
        }

        /** 插入区域信息 */
        List<SettleShopRuleRegionConfigVO> regionConfigVOList = shopRuleVO.getRegionConfigVOList();
        if(!CollectionUtils.isEmpty(regionConfigVOList)){
            List<SettleShopRuleRegionConfigDO> regionConfigDOList = new ArrayList<>();
            for(SettleShopRuleRegionConfigVO regionConfigVO : regionConfigVOList){
                SettleShopRuleRegionConfigDO configDO = BdUtil.do2bo(regionConfigVO, SettleShopRuleRegionConfigDO.class);
                configDO.setGmtCreate(ruleDO.getGmtCreate());
                configDO.setCreator(ruleDO.getCreator());
                configDO.setSettleRuleId(ruleDO.getId());
                regionConfigDOList.add(configDO);
            }
            regionConfigDOMapper.batchInsert(regionConfigDOList);
        }
    }
    /** 组装规则明细 */
    private void packageRuleItem(List<SettleShopRuleItemDO> ruleItemDOList, List<AddSettleShopRuleItemVO> itemVOList,
                                          Integer rebateStandard, SettleShopRuleDO shopRuleDO){
        if(CollectionUtils.isEmpty(itemVOList)){
            return;
        }
        for(AddSettleShopRuleItemVO ruleItemVO : itemVOList){
            SettleShopRuleItemDO itemDO = convertRuleItemVO(ruleItemVO, rebateStandard, shopRuleDO);
            ruleItemDOList.add(itemDO);
        }
    }
    /** 处理规则参数对象 */
    private SettleShopRuleItemDO convertRuleItemVO(AddSettleShopRuleItemVO ruleItemVO, Integer rebateStandard,
                                                 SettleShopRuleDO shopRuleDO){
        SettleShopRuleItemDO itemDO = BdUtil.do2bo(ruleItemVO, SettleShopRuleItemDO.class);
        itemDO.setId(null);
        itemDO.setCreator(shopRuleDO.getCreator());
        itemDO.setGmtCreate(shopRuleDO.getGmtCreate());
        itemDO.setSettleRuleId(shopRuleDO.getId());
        itemDO.setRebateStandard(rebateStandard);

        return itemDO;
    }



    /** ========== 奖励金模式，新增规则 ========== */
    private void checkItemForReward(AddSettleShopRuleItemVO itemVO){
        Assert.notNull(itemVO.getSettleConfigBasicId(), "请选择保费区间分组");
        Assert.notNull(itemVO.getCalculateMode(), "请选择计算方式");
//        Assert.notNull(itemVO.getEndTime(), "请选择生效起止日期");
    }
    private void checkAddShopRuleForReward(AddSettleShopRuleVO shopRuleVO){
        Assert.notNull(shopRuleVO, "参数不能为空");
        Assert.notNull(shopRuleVO.getInsuranceCompanyId(), "请选择保险公司");
        Assert.notNull(shopRuleVO.getSettleCondition(), "请选择结算条件");

        if(CollectionUtils.isEmpty(shopRuleVO.getBizRuleItemList())
                && CollectionUtils.isEmpty(shopRuleVO.getForceRuleItemList())){
            throwBizException("至少选择一种配置基准");
        }
        checkApplyRange(shopRuleVO);

        Set<Integer> basicIdSet = new HashSet<>();
        checkRuleItemList(shopRuleVO.getBizRuleItemList(), basicIdSet, true);
        checkRuleItemList(shopRuleVO.getForceRuleItemList(), basicIdSet, true);
        //检验保费区间组是否合法
        checkGroupIds(basicIdSet);
    }

    @Transactional
    @Override
    public boolean addSettleShopRuleForReward(AddSettleShopRuleVO shopRuleVO) {
        /** 入参校验 */
        checkAddShopRuleForReward(shopRuleVO);

        /** 插入主表数据 */
        shopRuleVO.setCooperationMode(CooperationModeEnum.GIVE_REWARD.getCode());
        SettleShopRuleDO shopRuleDO = saveShopRule(shopRuleVO);

        /** 处理，保存适用范围 */
        saveApplyRange(shopRuleVO, shopRuleDO);

        /** 组装规则明细，插入数据 */
        List<SettleShopRuleItemDO> ruleItemDOList = new ArrayList<>();
        packageRuleItemForReward(ruleItemDOList, shopRuleVO.getBizRuleItemList(),
                RebateStandardEnum.BIZ_INSURANCE.getCode(), shopRuleDO, shopRuleVO.getSettleCondition());
        packageRuleItemForReward(ruleItemDOList, shopRuleVO.getForceRuleItemList(),
                RebateStandardEnum.FORCE_INSURANCE.getCode(), shopRuleDO, shopRuleVO.getSettleCondition());
        shopRuleItemDOMapper.batchInsert(ruleItemDOList);

        return true;
    }
    private void packageRuleItemForReward(List<SettleShopRuleItemDO> ruleItemDOList, List<AddSettleShopRuleItemVO> itemVOList,
                                 Integer rebateStandard, SettleShopRuleDO shopRuleDO, Integer settleCondition){
        if(CollectionUtils.isEmpty(itemVOList)){
            return;
        }
        for(AddSettleShopRuleItemVO ruleItemVO : itemVOList){
            SettleShopRuleItemDO itemDO = convertRuleItemVO(ruleItemVO, rebateStandard, shopRuleDO);
            itemDO.setSettleCondition(settleCondition);
            itemDO.setFundType(FundTypeEnum.REWARD.getCode());
            itemDO.setRebateType(RebateTypeEnum.RATE.getCode());

            ruleItemDOList.add(itemDO);
        }

    }


    @Override
    public boolean querySettleConfigIsUse(Integer basicId) {
        if(basicId == null){
            throwBizException("参数不能为空");
        }
        Integer id = shopRuleItemDOMapper.selectShopItemCountByBasicId(basicId);
        if(id != null){
            return true;
        }
        return false;
    }


    /** 检查参数 */
    private void checkQueryShopRulePageParam(SettleShopRulePageParam param){
        Assert.notNull(param, "参数不能为空");
        Assert.notEmpty(param.getCooperationModes(), "缺少必要参数：合作模式");
        if(param.getShopId()!=null
                && (!StringUtils.isEmpty(param.getProvinceCode()) || !StringUtils.isEmpty(param.getCityCode()))){
            throwBizException("暂不支持地区+门店同时查询");
        }

        if("".equals(param.getCityCode())){
            param.setCityCode(null);
        }
        if("".equals(param.getProvinceCode())){
            param.setProvinceCode(null);
        }
    }
    @Override
    public PagingResult<SettleShopRuleVO> queryShopRulePage(SettleShopRulePageParam param) {
        /** 参数检查 */
        checkQueryShopRulePageParam(param);

        Integer pageSize = param.getPageSize();
        if(pageSize==null || pageSize<1){
            pageSize = 10;
            param.setPageSize(pageSize);
        }
        Integer pageIndex = param.getPageIndex();
        if(pageIndex==null || pageIndex<1){
            pageIndex = 1;
        }
        int offerSet = (pageIndex - 1) * pageSize;
        param.setOfferSet(offerSet);

        List<SettleShopRuleDO> shopRuleDOList;
        int total = 0;
        if(param.getShopId()==null){
            if(StringUtils.isEmpty(param.getProvinceCode()) && StringUtils.isEmpty(param.getCityCode())){
                shopRuleDOList = shopRuleDOMapper.selectShopRulePage(param);
                if(!shopRuleDOList.isEmpty()) {
                    total = shopRuleDOMapper.selectShopRuleCount(param);
                }
            }else{
                shopRuleDOList =shopRuleDOMapper.selectShopRuleForRegionPage(param);
                if(!shopRuleDOList.isEmpty()){
                    total = shopRuleDOMapper.selectShopRuleForRegionCount(param);
                }
            }
        }else{
            shopRuleDOList = shopRuleDOMapper.selectShopRuleForShopPage(param);
            if(!shopRuleDOList.isEmpty()){
                total = shopRuleDOMapper.selectShopRuleForShopCount(param);
            }
        }

        if(shopRuleDOList.isEmpty()){
            return PagingResult.wrapErrorResult("", "暂无数据");
        }

        List<SettleShopRuleVO> ruleVOList = packageSettleShopRuleVO(shopRuleDOList);
        return PagingResult.wrapSuccessfulResult(ruleVOList, total);
    }

    /** 组装前端需要的规则数据 */
    private List<SettleShopRuleVO> packageSettleShopRuleVO(List<SettleShopRuleDO> shopRuleDOList){
        List<SettleShopRuleVO> ruleVOList = new ArrayList<>();
        List<Integer> ruleIdList = new ArrayList<>();
        List<Integer> ruleIdsForRegion = new ArrayList<>();
        List<Integer> ruleIdsForShop = new ArrayList<>();
        for(SettleShopRuleDO shopRuleDO : shopRuleDOList){
            SettleShopRuleVO ruleVO = BdUtil.do2bo(shopRuleDO, SettleShopRuleVO.class);
            ruleVO.setCooperationModeName(insuranceDicBiz.getCooperationModeNameByDicId(ruleVO.getCooperationMode()));
            ruleVOList.add(ruleVO);

            ruleIdList.add(ruleVO.getId());

            if(ApplyRangeEnum.REGION.getCode().equals(shopRuleDO.getApplyRange())){
                ruleIdsForRegion.add(ruleVO.getId());
            }else if(ApplyRangeEnum.SHOP.getCode().equals(shopRuleDO.getApplyRange())){
                ruleIdsForShop.add(ruleVO.getId());
            }
        }

        Map<Integer, StringBuilder> applyRangeStringMap = new HashMap<>();
        if(!ruleIdsForRegion.isEmpty()) {
            /** 组装地区信息 */
            List<SettleShopRuleRegionConfigDO> regionConfigDOList = regionConfigDOMapper.selectByRuleIds(ruleIdsForRegion);
            for (SettleShopRuleRegionConfigDO regionConfigDO : regionConfigDOList) {
                Integer settleRuleId = regionConfigDO.getSettleRuleId();
                StringBuilder regionList = applyRangeStringMap.get(settleRuleId);
                if (regionList == null) {
                    regionList = new StringBuilder();
                    applyRangeStringMap.put(settleRuleId, regionList);
                }
                regionList.append("、").append(regionConfigDO.getCityName());
            }
        }

        if(!ruleIdsForShop.isEmpty()){
            /** 组装门店信息 */
            List<SettleShopRuleShopConfigDO> shopConfigDOList = shopConfigDOMapper.selectByRuleIds(ruleIdsForShop);
            for (SettleShopRuleShopConfigDO shopConfigDO : shopConfigDOList){
                Integer settleRuleId = shopConfigDO.getSettleRuleId();
                StringBuilder shopInfo = applyRangeStringMap.get(settleRuleId);
                if(shopInfo == null){
                    shopInfo = new StringBuilder();
                    applyRangeStringMap.put(settleRuleId, shopInfo);
                }
                shopInfo.append("、").append(shopConfigDO.getShopName());
                shopInfo.append(" ").append(shopConfigDO.getShopAccount());
            }
        }

        /** 组装规则明细 */
        List<SettleShopRuleConfigBasicVO> allConfigBasicVOList = new ArrayList<>(); //方便下面组装保费区间明细
        Set<Integer> configBasicIdSet = new HashSet<>();
        // 组装返点基准集合
        List<SettleShopRuleRebateStandardVO> rebateStandardList =
                packageRebateStandardList(ruleIdList, configBasicIdSet, allConfigBasicVOList);

        int size = rebateStandardList.size();
        for(SettleShopRuleVO ruleVO : ruleVOList){
            Integer ruleId = ruleVO.getId();
            //组装适用范围信息
            StringBuilder sb = applyRangeStringMap.get(ruleId);
            if(sb != null){
                sb.deleteCharAt(0);
                ruleVO.setApplyRangeString(sb.toString());
            }
            //组装返点基准信息
            for(int i=0; i<size; i++){
                SettleShopRuleRebateStandardVO standardVO = rebateStandardList.get(i);
                if(standardVO.getSettleRuleId().equals(ruleId)){
                    List<SettleShopRuleRebateStandardVO> rebateStandardVOs = ruleVO.getRebateStandardVOs();
                    if(rebateStandardVOs==null){
                        rebateStandardVOs = new ArrayList<>();
                        ruleVO.setRebateStandardVOs(rebateStandardVOs);
                    }
                    rebateStandardVOs.add(standardVO);

                    rebateStandardList.remove(i);
                    i--;
                    size--;

                    //奖励金模式，在最外层对象冗余一份结算条件，方便前端展示
                    if(ruleVO.getSettleConditionName()==null
                            && CooperationModeEnum.GIVE_REWARD.getCode().equals(ruleVO.getCooperationMode())){
                        ruleVO.setSettleConditionName(insuranceDicBiz.getSettleConditionNameByDicId(standardVO.getSettleCondition()));
                    }
                }
            }
        }

        /** 组装保费区间明细 */
        Map<Integer, List<SettleConfigItemVO>> configItemVOMap = packageConfigItemMap(configBasicIdSet);
        //因为对象是同一份，所以处理 allConfigBasicVOList，等价于处理 rebateStandardList 内部嵌套对象，但是更方便些
        for(SettleShopRuleConfigBasicVO shopRuleConfigBasicVO : allConfigBasicVOList){
            Integer settleConfigBasicId = shopRuleConfigBasicVO.getSettleConfigBasicId();
            List<SettleConfigItemVO> configItemVOList = configItemVOMap.get(settleConfigBasicId);
            if(configItemVOList==null){
                continue;
            }
            SettleConfigBasicVO configBasicVO = new SettleConfigBasicVO();
            configBasicVO.setId(settleConfigBasicId);
            configBasicVO.setGroupName(configItemVOList.get(0).getGroupName());
            configBasicVO.setItemVOList(configItemVOList);

            shopRuleConfigBasicVO.setSettleConfigBasicVO(configBasicVO);

            //todo 计算合并行数
            int itemSize = configItemVOList.size();
            shopRuleConfigBasicVO.setRowSpan(itemSize);
            SettleShopRuleRebateStandardVO rebateStandardVO = shopRuleConfigBasicVO.getRebateStandardVO();
            if(rebateStandardVO!=null){
                rebateStandardVO.setRowSpan(rebateStandardVO.getRowSpan()+itemSize);
            }
        }

        //todo 计算合并行数
        for(SettleShopRuleVO ruleVO : ruleVOList){
            List<SettleShopRuleRebateStandardVO> list = ruleVO.getRebateStandardVOs();
            if(CollectionUtils.isEmpty(list)){
                continue;
            }
            for(SettleShopRuleRebateStandardVO standardVO : list){
                int rowSpan = standardVO.getRowSpan();
                if(rowSpan==0){
                    rowSpan = 1;
                }
                ruleVO.setRowSpan(ruleVO.getRowSpan()+rowSpan);
            }
        }

        return ruleVOList;
    }

    /** 组装返点基准 list */
    private List<SettleShopRuleRebateStandardVO> packageRebateStandardList(List<Integer> ruleIdList, Set<Integer> configBasicIdSet,
                                                                                 List<SettleShopRuleConfigBasicVO> allConfigBasicVOList){
        List<SettleShopRuleItemDO> ruleItemDOList = shopRuleItemDOMapper.selectByRuleIds(ruleIdList);
        Collections.sort(ruleItemDOList, new Comparator<SettleShopRuleItemDO>() {
            @Override
            public int compare(SettleShopRuleItemDO o1, SettleShopRuleItemDO o2) {
                int flag = o1.getSettleRuleId().compareTo(o2.getSettleRuleId());
                if(flag==0){
                    return o1.getStartTime().compareTo(o2.getStartTime());
                }
                return flag;
            }
        });
        Map<String, SettleShopRuleRebateStandardVO> rebateStandardMap = new HashMap<>();
        for(SettleShopRuleItemDO ruleItemDO : ruleItemDOList){
            String key = ruleItemDO.getSettleRuleId() + "_" + ruleItemDO.getRebateStandard();
            SettleShopRuleRebateStandardVO rebateStandardVO = rebateStandardMap.get(key);
            List<SettleShopRuleConfigBasicVO> configBasicVOList;
            if(rebateStandardVO==null){
                rebateStandardVO = BdUtil.do2bo(ruleItemDO, SettleShopRuleRebateStandardVO.class);
                seRebateStandardEnumNames(rebateStandardVO);

                configBasicVOList = new ArrayList<>();
                rebateStandardVO.setConfigBasicVOList(configBasicVOList);
                rebateStandardMap.put(key, rebateStandardVO);
            }else{
                configBasicVOList = rebateStandardVO.getConfigBasicVOList();
            }
            SettleShopRuleConfigBasicVO configBasicVO = BdUtil.do2bo(ruleItemDO, SettleShopRuleConfigBasicVO.class);
            configBasicVOList.add(configBasicVO);

            allConfigBasicVOList.add(configBasicVO);
            configBasicIdSet.add(configBasicVO.getSettleConfigBasicId());

            //设置上层对象方便则装合并行数
            configBasicVO.setRebateStandardVO(rebateStandardVO);
        }
        return new ArrayList<>(rebateStandardMap.values());
    }
    private void seRebateStandardEnumNames(SettleShopRuleRebateStandardVO standard){
        standard.setRebateStandardName(insuranceDicBiz.getRebateStandardNameByDicId(standard.getRebateStandard()));
        standard.setSettleConditionName(insuranceDicBiz.getSettleConditionNameByDicId(standard.getSettleCondition()));
        standard.setFundTypeName(insuranceDicBiz.getFundTypeNameByDicId(standard.getFundType()));
        standard.setRebateTypeName(insuranceDicBiz.getRebateTypeNameByDicId(standard.getRebateType()));
        standard.setCalculateModeName(insuranceDicBiz.getCalculateModeNameByDicId(standard.getCalculateMode()));
    }

    /** 组装保费区间明细 map */
    private Map<Integer, List<SettleConfigItemVO>> packageConfigItemMap(Set<Integer> configBasicIdSet){
        List<SettleConfigItemExtendDO> configItemDOList = configItemDOMapper.selectItemByBasicIds(configBasicIdSet);
        Collections.sort(configItemDOList, new Comparator<SettleConfigItemExtendDO>() {
            @Override
            public int compare(SettleConfigItemExtendDO o1, SettleConfigItemExtendDO o2) {
                int flag = o1.getBasicId().compareTo(o2.getBasicId());
                if(flag==0){
                    return o1.getItemStartValue().compareTo(o2.getItemStartValue());
                }
                return flag;
            }
        });
        Map<Integer, List<SettleConfigItemVO>> configItemVOMap = new HashMap<>();
        for(SettleConfigItemExtendDO settleConfigItemDO : configItemDOList){
            Integer basicId = settleConfigItemDO.getBasicId();
            List<SettleConfigItemVO> configItemVOList = configItemVOMap.get(basicId);
            if(configItemVOList==null){
                configItemVOList = new ArrayList<>();
                configItemVOMap.put(basicId, configItemVOList);
            }
            SettleConfigItemVO configItemVO = BdUtil.do2bo(settleConfigItemDO, SettleConfigItemVO.class);
            configItemVO.setInsuranceTypeName(insuranceDicBiz.getInsuranceTypeNameByDicId(settleConfigItemDO.getInsuranceType()));
            configItemVO.setCalculateModeName(insuranceDicBiz.getCalculateModeNameByDicId(settleConfigItemDO.getCalculateMode()));
            configItemVOList.add(configItemVO);
        }
        return configItemVOMap;
    }

    @Override
    public boolean deleteShopRule(Integer id) {
        Assert.notNull(id, "参数不能为空");
        SettleShopRuleDO shopRuleDO = shopRuleDOMapper.selectByPrimaryKey(id);
        Assert.notNull(shopRuleDO, "数据不存在：id="+id);
        List<Integer> shopRuleIds = shopRuleItemDOMapper.selectItemIdListByRuleId(id);
        if(!CollectionUtils.isEmpty(shopRuleIds)){
            shopRuleItemDOMapper.batchDeleteShopRuleItemByIds(shopRuleIds);
        }
        shopRuleDO.setGmtModified(new Date());
        shopRuleDO.setModifier(shiroBiz.getCurrentUserRealName());
        shopRuleDO.setIsDeleted("Y");

        shopRuleDOMapper.updateByPrimaryKeySelective(shopRuleDO);

        return true;
    }


    /** ========== 修改规则配置 ========== */
    private void checkModifyShopRuleVO(ModifyShopRuleVO modifyShopRuleVO, boolean forReward){
        Assert.notNull(modifyShopRuleVO, "参数不能为空");
        if(forReward){
            Assert.notNull(modifyShopRuleVO.getSettleCondition(), "请选择结算条件");
        }
        Integer id = modifyShopRuleVO.getId();
        Assert.notNull(id, "缺少必要参数id");
        SettleShopRuleDO shopRuleDO = shopRuleDOMapper.selectByPrimaryKey(id);
        Assert.notNull(shopRuleDO, "数据不存在：id="+id);

        if(modifyShopRuleVO.getPackageRuleItem()==null && CollectionUtils.isEmpty(modifyShopRuleVO.getBizRuleItemList())
                && CollectionUtils.isEmpty(modifyShopRuleVO.getForceRuleItemList())){
            throwBizException("至少选择一种配置基准");
        }

        Set<Integer> basicIdSet = new HashSet<>();
        checkRuleItemList(modifyShopRuleVO.getBizRuleItemList(), basicIdSet, forReward);
        checkRuleItemList(modifyShopRuleVO.getForceRuleItemList(), basicIdSet, forReward);
        //检验保费区间组是否合法
        checkGroupIds(basicIdSet);

        AddSettleShopRuleItemVO ruleItemVO = modifyShopRuleVO.getPackageRuleItem();
        if(ruleItemVO != null){
            checkItemForPackage(ruleItemVO);
            checkItemEndTime(ruleItemVO);
            ruleItemVO.setStartTime(handleStartDate(new Date()));
            ruleItemVO.setCalculateMode(CalculateModeEnum.SINGLE.getCode());
            ruleItemVO.setSettleConfigBasicId(0);
        }

    }

    @Transactional
    @Override
    public boolean modifyShopRule(ModifyShopRuleVO modifyShopRuleVO) {
        /** 参数校验 */
        checkModifyShopRuleVO(modifyShopRuleVO, false);

        /** 如果修改地区配置，地区配置表全部删除，重新插入数据 */
        handleRegionConfig(modifyShopRuleVO);

        /** item表全部删除，重新插入数据 */
        SettleShopRuleDO shopRuleDO = deleteShopRuleItem(modifyShopRuleVO.getId());
        // 组装规则明细，插入数据
        List<SettleShopRuleItemDO> ruleItemDOList = new ArrayList<>();
        packageRuleItem(ruleItemDOList, modifyShopRuleVO.getBizRuleItemList(), RebateStandardEnum.BIZ_INSURANCE.getCode(), shopRuleDO);
        packageRuleItem(ruleItemDOList, modifyShopRuleVO.getForceRuleItemList(), RebateStandardEnum.FORCE_INSURANCE.getCode(), shopRuleDO);
        // 处理服务包规则
        AddSettleShopRuleItemVO shopRuleItemVO = modifyShopRuleVO.getPackageRuleItem();
        if(shopRuleItemVO != null) {
            SettleShopRuleItemDO itemDO = convertRuleItemVO(shopRuleItemVO, RebateStandardEnum.PACKAGE.getCode(), shopRuleDO);
            ruleItemDOList.add(itemDO);
        }
        shopRuleItemDOMapper.batchInsert(ruleItemDOList);

        return true;
    }
    private void handleRegionConfig(ModifyShopRuleVO modifyShopRuleVO){
        List<SettleShopRuleRegionConfigVO> regionConfigVOList = modifyShopRuleVO.getRegionConfigVOList();
        if(CollectionUtils.isEmpty(regionConfigVOList)){
            return;
        }
        Date date = new Date();
        String operator = shiroBiz.getCurrentUserRealName();
        DeleteRegionConfigBO deleteRegionConfigBO = new DeleteRegionConfigBO();
        deleteRegionConfigBO.setGmtModified(date);
        deleteRegionConfigBO.setModifier(operator);
        deleteRegionConfigBO.setSettleRuleId(modifyShopRuleVO.getId());
        regionConfigDOMapper.deleteBySettleRuleId(deleteRegionConfigBO);

        List<SettleShopRuleRegionConfigDO> regionConfigDOList = new ArrayList<>();
        for(SettleShopRuleRegionConfigVO regionConfigVO : regionConfigVOList){
            SettleShopRuleRegionConfigDO configDO = BdUtil.do2bo(regionConfigVO, SettleShopRuleRegionConfigDO.class);
            configDO.setGmtCreate(date);
            configDO.setCreator(operator);
            configDO.setSettleRuleId(modifyShopRuleVO.getId());
            regionConfigDOList.add(configDO);
        }
        regionConfigDOMapper.batchInsert(regionConfigDOList);
    }
    private SettleShopRuleDO deleteShopRuleItem(Integer settleRuleId){
        Date date = new Date();
        String operator = shiroBiz.getCurrentUserRealName();
        DeleteShopRuleItemBO deleteShopRuleItemBO = new DeleteShopRuleItemBO();
        deleteShopRuleItemBO.setGmtModified(date);
        deleteShopRuleItemBO.setModifier(operator);
        deleteShopRuleItemBO.setSettleRuleId(settleRuleId);
        shopRuleItemDOMapper.deleteItemBySettleRuleId(deleteShopRuleItemBO);

        SettleShopRuleDO shopRuleDO = new SettleShopRuleDO();
        shopRuleDO.setId(settleRuleId);
        shopRuleDO.setGmtCreate(date);
        shopRuleDO.setCreator(operator);

        return shopRuleDO;
    }

    @Transactional
    @Override
    public boolean modifyShopRuleForReward(ModifyShopRuleVO modifyShopRuleVO) {
        /** 参数校验 */
        checkModifyShopRuleVO(modifyShopRuleVO, true);

        /** 如果修改地区配置，地区配置表全部删除，重新插入数据 */
        handleRegionConfig(modifyShopRuleVO);

        /** item表全部删除，重新插入数据 */
        SettleShopRuleDO shopRuleDO = deleteShopRuleItem(modifyShopRuleVO.getId());
        // 组装规则明细，插入数据
        List<SettleShopRuleItemDO> ruleItemDOList = new ArrayList<>();
        packageRuleItemForReward(ruleItemDOList, modifyShopRuleVO.getBizRuleItemList(),
                RebateStandardEnum.BIZ_INSURANCE.getCode(), shopRuleDO, modifyShopRuleVO.getSettleCondition());
        packageRuleItemForReward(ruleItemDOList, modifyShopRuleVO.getForceRuleItemList(),
                RebateStandardEnum.FORCE_INSURANCE.getCode(), shopRuleDO, modifyShopRuleVO.getSettleCondition());
        shopRuleItemDOMapper.batchInsert(ruleItemDOList);

        return true;
    }

    @Override
    public List<SettleShopRuleItemDO> getRuleItemList(Integer calculateMode, Date searchTime) {
        if (calculateMode == null || searchTime == null){
            log.info("params is null.calculateMode:{},searchTime:{}",calculateMode,searchTime);
            return Lists.newArrayList();
        }
        SettleShopRuleItemDO searchDO = new SettleShopRuleItemDO();
        searchDO.setCalculateMode(calculateMode);
        List<SettleShopRuleItemDO> sqlResultList = shopRuleItemDOMapper.selectByDO(searchDO);
        Iterator<SettleShopRuleItemDO> iterator = sqlResultList.iterator();
        while (iterator.hasNext()){
            SettleShopRuleItemDO ruleItemDO = iterator.next();
            Date startTime = ruleItemDO.getStartTime();
            Date endTime = ruleItemDO.getEndTime();
            // 当前日期比开始日期小,remove
            if(searchTime.compareTo(startTime) < 0){
                iterator.remove();
                continue;
            }
            // 当前日期比结束日期大,remove
            if(endTime != null && searchTime.compareTo(endTime) > 0){
                iterator.remove();
                continue;
            }
        }


        return sqlResultList;
    }


    @Override
    public SettleRateBO getSettleRate(ShopSettleRateQueryParam param) {
        if(param==null){
            return null;
        }
        if(param.getInsuranceCompanyId()==null || param.getCooperationMode()==null
                || param.getRebateStandard()==null ){
            return null;
        }

        /** 确定规则id */
        Integer settleRuleId = null;
        if(param.getShopId()==null){
            if(!StringUtils.isEmpty(param.getCityCode())){
                settleRuleId = shopRuleDOMapper.selectIdWithRegionConfig(param);
            }
        }else{
            settleRuleId = shopRuleDOMapper.selectIdWithShopConfig(param);
            if(settleRuleId==null && !StringUtils.isEmpty(param.getCityCode())){
                settleRuleId = shopRuleDOMapper.selectIdWithRegionConfig(param);
            }
        }
        if(settleRuleId==null){
            return null;
        }

        /** 根据规则id，查询比例 */
        SettleShopRuleItemDO ruleItemDO = getRuleItem(settleRuleId, param.getRebateStandard());
        if(ruleItemDO==null){
            return null;
        }
        SettleRateBO settleRateBO = new SettleRateBO();
        settleRateBO.setSettleCondition(ruleItemDO.getSettleCondition());

        if(RebateTypeEnum.SERVICE_FEE.getCode().equals(ruleItemDO.getRebateType())){
            return settleRateBO;
        }

        BigDecimal settleRate = getSettleConfigItemRate(ruleItemDO.getSettleConfigBasicId(), param.getInsuredFee());
        if(settleRate!=null){
            settleRateBO.setSettleRate(settleRate);
        }

        return settleRateBO;
    }
    private SettleShopRuleItemDO getRuleItem(Integer settleRuleId, Integer rebateStandard){
        List<Integer> ruleIdList = new ArrayList<>();
        ruleIdList.add(settleRuleId);
        List<SettleShopRuleItemDO> itemDOList = shopRuleItemDOMapper.selectByRuleIds(ruleIdList);
        if(itemDOList.isEmpty()){
            return null;
        }
        //判断有效期
        Date now = new Date();
        for(SettleShopRuleItemDO itemDO : itemDOList){
            if(rebateStandard.equals(itemDO.getRebateStandard())){
                Date startTime = itemDO.getStartTime();
                if(startTime==null){ //错误数据，直接无视
                    continue;
                }
                if(now.compareTo(startTime)<0){
                    continue;
                }
                Date endTime = itemDO.getEndTime();
                if(endTime==null || now.compareTo(endTime)<=0){
                    return itemDO;
                }
            }
        }
        return null;
    }
    private BigDecimal getSettleConfigItemRate(Integer basicId, BigDecimal insuredFee){
        if(basicId==null || basicId<1 || insuredFee==null){
            return null;
        }
        List<SettleConfigItemDO> itemDOList = configItemDOMapper.selectItemDOListByBasicId(basicId);
        if(itemDOList.isEmpty()){
            return null;
        }
        for(SettleConfigItemDO itemDO : itemDOList){
            BigDecimal itemStartValue = itemDO.getItemStartValue();
            if(insuredFee.compareTo(itemStartValue)<0){
                continue;
            }
            BigDecimal itemEndValue = itemDO.getItemEndValue();
            if(itemEndValue==null || insuredFee.compareTo(itemEndValue)<=0){
                return itemDO.getItemRate();
            }
        }
        return null;
    }

    @Override
    public SettleShopRuleVO getSettleShopRule(Integer id) {
        Assert.notNull(id, "参数不能为空");
        SettleShopRuleDO shopRuleDO = shopRuleDOMapper.selectByPrimaryKey(id);
        Assert.notNull(shopRuleDO, "未查到数据");

        SettleShopRuleVO ruleVO = BdUtil.do2bo(shopRuleDO, SettleShopRuleVO.class);
        List<Integer> ruleIdList = new ArrayList<>();
        ruleIdList.add(id);
        if(ApplyRangeEnum.REGION.getCode().equals(shopRuleDO.getApplyRange())){
            /** 组装地区信息 */
            List<SettleShopRuleRegionConfigDO> regionConfigDOList = regionConfigDOMapper.selectByRuleIds(ruleIdList);
            if(!regionConfigDOList.isEmpty()) {
                ruleVO.setRegionConfigVOList(BdUtil.do2bo4List(regionConfigDOList, SettleShopRuleRegionConfigVO.class));
            }
        }else if(ApplyRangeEnum.SHOP.getCode().equals(shopRuleDO.getApplyRange())){
            /** 组装门店信息 */
            List<SettleShopRuleShopConfigDO> shopConfigDOList = shopConfigDOMapper.selectByRuleIds(ruleIdList);
            if(!shopConfigDOList.isEmpty()){
                ruleVO.setShopConfigVO(BdUtil.do2bo(shopConfigDOList.get(0), SettleShopRuleShopConfigVO.class));
            }
        }

        /** 组装规则明细 */
        List<SettleShopRuleConfigBasicVO> allConfigBasicVOList = new ArrayList<>(); //方便下面组装保费区间明细
        Set<Integer> configBasicIdSet = new HashSet<>();
        // 组装返点基准集合
        List<SettleShopRuleRebateStandardVO> rebateStandardList =
                packageRebateStandardList(ruleIdList, configBasicIdSet, allConfigBasicVOList);
        ruleVO.setRebateStandardVOs(rebateStandardList);

        //奖励金模式，需要在最外层设置结算条件方便前端展示
        if(CooperationModeEnum.GIVE_REWARD.getCode().equals(ruleVO.getCooperationMode())) {
            ruleVO.setSettleCondition(rebateStandardList.get(0).getSettleCondition());
        }

        /** 组装保费区间明细 */
        Map<Integer, List<SettleConfigItemVO>> configItemVOMap = packageConfigItemMap(configBasicIdSet);
        //因为对象是同一份，所以处理 allConfigBasicVOList，等价于处理 rebateStandardList 内部嵌套对象，但是更方便些
        for(SettleShopRuleConfigBasicVO shopRuleConfigBasicVO : allConfigBasicVOList){
            Integer settleConfigBasicId = shopRuleConfigBasicVO.getSettleConfigBasicId();
            List<SettleConfigItemVO> configItemVOList = configItemVOMap.get(settleConfigBasicId);
            if(configItemVOList==null){
                continue;
            }
            SettleConfigItemVO configItemVO = configItemVOList.get(0);
            SettleConfigBasicVO configBasicVO = BdUtil.do2bo(configItemVO, SettleConfigBasicVO.class);
            configBasicVO.setId(settleConfigBasicId);
            configBasicVO.setItemVOList(configItemVOList);

            shopRuleConfigBasicVO.setSettleConfigBasicVO(configBasicVO);
        }

        return ruleVO;
    }

    @Override
    public List<Integer> getRuleIdListByCooperationMode(Integer cooperationMode,Integer insuranceCompanyId) {
        if(cooperationMode == null){
            throw new BusinessCheckException("001","参数异常");
        }

        return shopRuleDOMapper.selectRuleIdListByCooperationMode(cooperationMode,insuranceCompanyId);
    }
}
