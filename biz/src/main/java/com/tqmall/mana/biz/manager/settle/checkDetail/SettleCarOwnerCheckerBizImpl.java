package com.tqmall.mana.biz.manager.settle.checkDetail;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.settle.SettleCarOwnerAmountSumBO;
import com.tqmall.mana.beans.VO.settle.SettleCarOwnerBizPagingResult;
import com.tqmall.mana.beans.VO.settle.SettleCarOwnerCheckerDetailVO;
import com.tqmall.mana.beans.VO.settle.SettleCarOwnerInsuranceTypeDetailVO;
import com.tqmall.mana.beans.entity.settle.SettleCarOwnerCheckDetailDO;
import com.tqmall.mana.beans.entity.settle.SettleFormDO;
import com.tqmall.mana.beans.entity.settle.SettleServiceCheckDetailDO;
import com.tqmall.mana.beans.param.settle.SettleCarOwnerCheckerBizParam;
import com.tqmall.mana.beans.param.settle.SettleCarOwnerCheckerForBrilliantBizParam;
import com.tqmall.mana.component.annotation.lock.ManaLock;
import com.tqmall.mana.component.annotation.lock.ManaLockKey;
import com.tqmall.mana.component.enums.insurance.dict.AgentTagEnum;
import com.tqmall.mana.component.enums.insurance.dict.ConfirmMoneyStatusEnum;
import com.tqmall.mana.component.enums.insurance.dict.InsuranceTypeEnum;
import com.tqmall.mana.component.enums.insurance.dict.TqmallPayStatusEnum;
import com.tqmall.mana.component.redis.RedisKeyBean;
import com.tqmall.mana.component.util.BeanUtil;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.dao.mapper.settle.SettleCarOwnerCheckDetailDOMapper;
import com.tqmall.mana.dao.mapper.settle.SettleFormDOMapper;
import com.tqmall.mana.dao.mapper.settle.SettleServiceCheckDetailDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zengjinju on 17/1/21.
 */
@Service
@Slf4j
public class SettleCarOwnerCheckerBizImpl implements SettleCarOwnerCheckerBiz {

    @Autowired
    private SettleCarOwnerCheckDetailDOMapper settleCarOwnerCheckDetailDOMapper;
    @Autowired
    private SettleServiceCheckDetailDOMapper settleServiceCheckDetailDOMapper;
    @Autowired
    private SettleFormDOMapper settleFormDOMapper;


    private final static Integer MAX_SIZE = 50;


    @Override
    public SettleCarOwnerBizPagingResult carOwnerCheckerForERP(SettleCarOwnerCheckerBizParam param) {
        Assert.notNull(param, "SettleCarOwnerCheckerBizParam 不能为空");
        SettleCarOwnerBizPagingResult pagingResult = new SettleCarOwnerBizPagingResult();
        Map<String, Object> searchParam = BeanUtil.param2Map(param);
        searchParam.put("start", (param.getPage() - 1) * param.getPageSize());
        searchParam.put("limit", param.getPageSize());
        Integer totalNum = settleCarOwnerCheckDetailDOMapper.getCountByCondation(searchParam);
        if (totalNum == 0) {
            log.info("根据条件param{}" + JsonUtil.objectToStr(param) + "查询的数据为空");
            pagingResult.setPagingResult(PagingResult.wrapSuccessfulResult(new ArrayList<SettleCarOwnerCheckerDetailVO>(), totalNum));
            return pagingResult;
        }
        List<SettleCarOwnerCheckerDetailVO> content = getCarOwnerCheckerDetailList(searchParam);
        pagingResult.setPagingResult(PagingResult.wrapSuccessfulResult(content, totalNum));
        //计算1期收款金额，2期收款金额，应交保费总额
        SettleCarOwnerAmountSumBO settleCarOwnerAmountSumBO = settleCarOwnerCheckDetailDOMapper.getInsuranceFeeTotalAmount(searchParam);
//        BigDecimal payableTotalAmount = settleFormDOMapper.getPayableTotalAmount(searchParam);
        pagingResult.setFirstPayTotalAmount(settleCarOwnerAmountSumBO.getFirstPayTotalAmount());
        pagingResult.setSecondPayTotalAmount(settleCarOwnerAmountSumBO.getSecondPayTotalAmount());
        pagingResult.setTotalPayAbleInsuredFee(settleCarOwnerAmountSumBO.getSyTotalPayableInsuredFee().add(settleCarOwnerAmountSumBO.getJqTotalPayableInsuredFee()));
        pagingResult.setPayableTotalAmount(pagingResult.getTotalPayAbleInsuredFee().add(settleCarOwnerAmountSumBO.getTotalJqTaxFee()));
        return pagingResult;
    }


    private List<SettleCarOwnerCheckerDetailVO> getCarOwnerCheckerDetailList(Map<String, Object> searchParam) {
        List<SettleCarOwnerCheckerDetailVO> content = new ArrayList<>(MAX_SIZE);
        List<String> insuranceOrderSnList = settleCarOwnerCheckDetailDOMapper.getInsuranceOrderSnForPage(searchParam);
        List<SettleCarOwnerCheckDetailDO> carOwnerCheckDOList = settleCarOwnerCheckDetailDOMapper.getCarOwnerCheckDetailForPageByInsuranceOrderSnList(insuranceOrderSnList);
        Map<String, SettleCarOwnerCheckerDetailVO> carOwnerCheckVOMap = new HashMap<>(MAX_SIZE);
        for (SettleCarOwnerCheckDetailDO settleCarOwnerCheckDetailDO : carOwnerCheckDOList) {
            if (!carOwnerCheckVOMap.containsKey(settleCarOwnerCheckDetailDO.getInsuranceOrderSn())) {
                SettleCarOwnerCheckerDetailVO settleCarOwnerCheckerDetailVO = new SettleCarOwnerCheckerDetailVO();
                BeanUtils.copyProperties(settleCarOwnerCheckDetailDO, settleCarOwnerCheckerDetailVO);
                settleCarOwnerCheckerDetailVO.setAgentTagName(AgentTagEnum.codeDescription(settleCarOwnerCheckDetailDO.getAgentTag()));
                carOwnerCheckVOMap.put(settleCarOwnerCheckDetailDO.getInsuranceOrderSn(), settleCarOwnerCheckerDetailVO);
            }
        }
        //获取服务包sn的列表
        List<String> settleServiceSnList = Lists.transform(carOwnerCheckDOList, new Function<SettleCarOwnerCheckDetailDO, String>() {
            @Override
            public String apply(SettleCarOwnerCheckDetailDO settleCarOwnerCheckDetailDO) {
                return settleCarOwnerCheckDetailDO.getSettleServiceSn();
            }
        });
        List<SettleServiceCheckDetailDO> serviceDOList = settleServiceCheckDetailDOMapper.selectByOrderSnList(settleServiceSnList);
        Map<String, SettleServiceCheckDetailDO> serviceMap = new HashMap<>(MAX_SIZE);
        for (SettleServiceCheckDetailDO serviceDO : serviceDOList) {
            if (!serviceMap.containsKey(serviceDO.getSettlePackageOrderSn())) {
                serviceMap.put(serviceDO.getSettlePackageOrderSn(), serviceDO);
            }
        }
        List<SettleFormDO> settleFormDOList = settleFormDOMapper.getSettleFormListByInsuranceOrderSn(insuranceOrderSnList);
        for (String key : carOwnerCheckVOMap.keySet()) {
            SettleCarOwnerCheckerDetailVO settleCarOwnerCheckerDetailVO = carOwnerCheckVOMap.get(key);
            SettleServiceCheckDetailDO serviceCheckDetailDO = serviceMap.get(settleCarOwnerCheckerDetailVO.getSettleServiceSn());
            //设置服务包的一些相关的信息
            if (serviceCheckDetailDO != null) {
                settleCarOwnerCheckerDetailVO.setSettlePackageName(serviceCheckDetailDO.getSettlePackageName());
                settleCarOwnerCheckerDetailVO.setSettlePackagePrice(serviceCheckDetailDO.getSettlePackagePrice());
            }
            //当买服务包送保险模式中的尾款还没有支付的时候，这个时候取settle_car_owner_check_detail表中的值，这个时候只为展示用
            SettleCarOwnerInsuranceTypeDetailVO jqInsuranceTypeDetail = new SettleCarOwnerInsuranceTypeDetailVO();
            jqInsuranceTypeDetail.setInsuranceFee(settleCarOwnerCheckerDetailVO.getJqPayableInsuredFee());
            jqInsuranceTypeDetail.setTaxFee(settleCarOwnerCheckerDetailVO.getJqTaxFee());
            jqInsuranceTypeDetail.setInsuranceTypeId(InsuranceTypeEnum.FORCE_INSURANCE.getCode());
            SettleCarOwnerInsuranceTypeDetailVO syInsuranceTypeDetail = new SettleCarOwnerInsuranceTypeDetailVO();
            syInsuranceTypeDetail.setInsuranceFee(settleCarOwnerCheckerDetailVO.getSyPayableInsuredFee());
            syInsuranceTypeDetail.setInsuranceTypeId(InsuranceTypeEnum.BIZ_INSURANCE.getCode());
            settleCarOwnerCheckerDetailVO.getInsuranceTypeDetailVOList().add(jqInsuranceTypeDetail);
            settleCarOwnerCheckerDetailVO.getInsuranceTypeDetailVOList().add(syInsuranceTypeDetail);
            carOwnerCheckVOMap.put(key, settleCarOwnerCheckerDetailVO);
        }
        String orderSn = "";
        for (SettleFormDO settleFormDO : settleFormDOList) {
            SettleCarOwnerCheckerDetailVO settleCarOwnerCheckerDetailVO = carOwnerCheckVOMap.get(settleFormDO.getInsuranceOrderSn());
            if (settleCarOwnerCheckerDetailVO != null) {
                //下面的逻辑对同一个insuranceOrderSn只进行一次设置
//                if (!orderSn.equals(settleFormDO.getInsuranceOrderSn())) {
//                    settleCarOwnerCheckerDetailVO.setAgentAccount(settleFormDO.getAgentAccount());
//                    settleCarOwnerCheckerDetailVO.setCooperationModeId(settleFormDO.getCooperationModeId());
//                }
                //设置险种的一些相关信息
                for(SettleCarOwnerInsuranceTypeDetailVO insuranceTypeDetailVO : settleCarOwnerCheckerDetailVO.getInsuranceTypeDetailVOList()){
                    if(insuranceTypeDetailVO.getInsuranceTypeId().equals(settleFormDO.getInsuranceTypeId())){
                        insuranceTypeDetailVO.setInsuranceFee(settleFormDO.getInsuredFee());
                        insuranceTypeDetailVO.setTaxFee(settleFormDO.getTaxFee());
                        insuranceTypeDetailVO.setInsuredFormNo(settleFormDO.getInsuredFormNo());
                    }
                }
                carOwnerCheckVOMap.put(settleFormDO.getInsuranceOrderSn(), settleCarOwnerCheckerDetailVO);
//                orderSn = settleFormDO.getInsuranceOrderSn();
            }
        }
        content.addAll(carOwnerCheckVOMap.values());
        Collections.sort(content, new Comparator<SettleCarOwnerCheckerDetailVO>() {
            @Override
            public int compare(SettleCarOwnerCheckerDetailVO o1, SettleCarOwnerCheckerDetailVO o2) {
                if (o1.getGmtFirstPaid() == null || o2.getGmtFirstPaid() == null) {
                    return -1;
                }
                int result = o1.getGmtFirstPaid().compareTo(o2.getGmtFirstPaid());
                if (result > 0) {
                    result = -1;
                } else if (result < 0) {
                    result = 1;
                }
                return result;
            }
        });
        return content;
    }

    @Override
    @ManaLock(lockKeyName = RedisKeyBean.LOCK_UPDATE_CAR_OWNER_CONFIRM_MONEY_PAID)
    public Result<Boolean> updateConfirmMoneyPaidStatus(List<Integer> idList, @ManaLockKey String operator) {
        if (CollectionUtils.isEmpty(idList)) {
            log.error("idList为空!");
            return Result.wrapSuccessfulResult(Boolean.FALSE);
        }
        if (StringUtils.isEmpty(operator)) {
            log.error("operator不能为空");
            return Result.wrapSuccessfulResult(Boolean.FALSE);
        }
        List<SettleCarOwnerCheckDetailDO> carOwnerCheckDetailDOList = settleCarOwnerCheckDetailDOMapper.getCarOwnerCheckDetailDOByIdListAndTqmallGetPaidStatus(idList, 1);
        List<Integer> list = Lists.transform(carOwnerCheckDetailDOList, new Function<SettleCarOwnerCheckDetailDO, Integer>() {
            @Override
            public Integer apply(SettleCarOwnerCheckDetailDO settleCarOwnerCheckDetailDO) {
                return settleCarOwnerCheckDetailDO.getId();
            }
        });
        if (!CollectionUtils.isEmpty(carOwnerCheckDetailDOList)) {
            String listStr = JsonUtil.objectToStr(list);
            log.info("id={}获取的数据有些已被更新", listStr);
            return Result.wrapSuccessfulResult("id=" + listStr + "获取的数据有些已被更新", Boolean.FALSE);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("operator", operator);
        map.put("idList", idList);
        map.put("status", ConfirmMoneyStatusEnum.HAS_CONFIRMED.getCode());
        Integer effectedRows = settleCarOwnerCheckDetailDOMapper.updateConfirmMoneyStatusByIds(map);
        return Result.wrapSuccessfulResult(effectedRows > 0);
    }

    @Override
    @ManaLock(lockKeyName = RedisKeyBean.LOCK_CAR_OWNER_REVIEW)
    public Result<Boolean> reviewPayStatus(Integer id, @ManaLockKey String operator) {
        Assert.notNull(id, "id不能为空");
        Assert.hasLength(operator, "操作人不能为空");

        SettleCarOwnerCheckDetailDO detailDO = settleCarOwnerCheckDetailDOMapper.selectByPrimaryKey(id);
        Assert.notNull(detailDO, "未查到数据，id："+id);

        Integer status = detailDO.getIsTqmallPayStatus();
        Assert.isTrue(TqmallPayStatusEnum.NOT_AUDIT.getCode().equals(status), "审核失败，非法状态："+status+"，数据id："+id);

        Map<String, Object> map = new HashMap<>();
        map.put("operator", operator);
        map.put("id", id);
        map.put("status", TqmallPayStatusEnum.NOT_PAY.getCode());
        return Result.wrapSuccessfulResult(settleCarOwnerCheckDetailDOMapper.updatePayStatusById(map) > 0);
    }

    @Override
    public List<SettleCarOwnerCheckerDetailVO> exportSettleCarOwnerChekerList(SettleCarOwnerCheckerBizParam param) {
        Assert.notNull(param, "SettleCarOwnerCheckerBizParam不能为空");
        Map<String, Object> searchParam = BeanUtil.param2Map(param);
        Integer totalCount = settleCarOwnerCheckDetailDOMapper.getCountByCondation(searchParam);
        List<SettleCarOwnerCheckerDetailVO> content = new ArrayList<>();
        if (totalCount == 0) {
            log.info("根据查询条件获取的数据不存在，param=" + JsonUtil.objectToStr(param));
            return content;
        }
        int totalPages = (int) Math.ceil(totalCount.doubleValue() / Double.valueOf(param.getPageSize()));
        for (int i = 1; i <= totalPages; i++) {
            searchParam.put("start", (i - 1) * param.getPageSize());
            searchParam.put("offset", param.getPageSize());
            List<SettleCarOwnerCheckerDetailVO> list = getCarOwnerCheckerDetailList(searchParam);
            content.addAll(list);
        }
        return content;
    }

    @Override
    public Boolean updateByInsuranceOrderSn(SettleCarOwnerCheckerForBrilliantBizParam param) {
        Assert.notNull(param, "SettleCarOwnerCheckerForBrilliantBizParam不能为空");
        if (param.getJqPayableInsuredFee() == null) {
            param.setJqPayableInsuredFee(BigDecimal.ZERO);
        }
        if (param.getSyPayableInsuredFee() == null) {
            param.setSyPayableInsuredFee(BigDecimal.ZERO);
        }
        int effectiveRows = settleCarOwnerCheckDetailDOMapper.updateByInsuranceOrderSn(param);
        return effectiveRows > 0;
    }
}
