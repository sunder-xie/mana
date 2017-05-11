package com.tqmall.mana.biz.manager.settle.checkDetail;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.mana.beans.BO.cashcoupon.CashCouponRuleConfigBO;
import com.tqmall.mana.beans.BO.settle.SettleFeeFormulaConfigBO;
import com.tqmall.mana.beans.BO.settle.ShopSettleDetailPackageModeBO;
import com.tqmall.mana.beans.BO.settle.ShopSettleDetailRewardModeBO;
import com.tqmall.mana.beans.entity.settle.*;
import com.tqmall.mana.beans.entity.settle.extend.ShopSettleDetailDO;
import com.tqmall.mana.beans.entity.settle.extend.ShopSettleStatisticsDO;
import com.tqmall.mana.beans.param.settle.BalanceStatusModifyPO;
import com.tqmall.mana.beans.param.settle.SettleFeeModifyPO;
import com.tqmall.mana.beans.param.settle.SettleShopCheckDetailQueryPO;
import com.tqmall.mana.beans.param.settle.ShopSettleDetailQueryPO;
import com.tqmall.mana.beans.param.settle.*;
import com.tqmall.mana.beans.param.settle.finance.WithdrawRewardPO;
import com.tqmall.mana.biz.manager.coupon.CashCouponRuleConfigBiz;
import com.tqmall.mana.biz.manager.insurance.InsuranceDicBiz;
import com.tqmall.mana.biz.manager.settle.config.SettleFormulaConfigBiz;
import com.tqmall.mana.biz.manager.settle.config.SettleRateConfigBiz;
import com.tqmall.mana.component.enums.FormulaVariableEnum;
import com.tqmall.mana.component.enums.YesNoEnum;
import com.tqmall.mana.component.enums.insurance.dict.*;
import com.tqmall.mana.component.exception.BusinessException;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.mana.ManaUtil;
import com.tqmall.mana.dao.mapper.settle.SettleFormDOMapper;
import com.tqmall.mana.dao.mapper.settle.SettleServiceCheckDetailDOMapper;
import com.tqmall.mana.dao.mapper.settle.SettleShopCheckDetailDOMapper;
import com.tqmall.mana.dao.mapper.settle.extend.SettleShopDetailMapper;
import com.tqmall.mana.external.dubbo.finance.ExtFcInsuranceService;
import com.tqmall.mana.external.dubbo.insurance.ExtCashCouponService;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by zxg on 17/1/17.
 * 19:52
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Slf4j
@Service
public class SettleShopCheckBizImpl implements SettleShopCheckBiz {
    @Autowired
    private InsuranceDicBiz insuranceDicBiz;
    @Autowired
    private SettleShopDetailMapper settleShopDetailMapper;
    @Autowired
    private SettleShopCheckDetailDOMapper shopCheckDetailDOMapper;
    @Autowired
    private SettleServiceCheckDetailDOMapper settleServiceCheckDetailDOMapper;
    @Autowired
    private ExtFcInsuranceService extFcInsuranceService;
    @Autowired
    private SettleFormulaConfigBiz formulaConfigBiz;
    @Autowired
    private SettleFormDOMapper settleFormDOMapper;
    @Autowired
    private SettleRateConfigBiz rateConfigBiz;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private CashCouponRuleConfigBiz cashCouponRuleConfigBiz;
    @Autowired
    private ExtCashCouponService extCashCouponService;


    @Override
    public PagingResult<ShopSettleDetailRewardModeBO> shopSettleDetailRewardModePage(ShopSettleDetailQueryPO param) {
        Assert.notNull(param, "参数不能为空");
        //处理分页参数
        param.handlePageParam();
        //奖励金
        param.setCooperationModeId(CooperationModeEnum.GIVE_REWARD.getCode());
//        param.setSettleRuleType(SettleRuleTypeEnum.REWARD.getCode());
        param.setSettleRuleType(SettleRuleTypeEnum.CASH.getCode());

        int total = 0;
        List<ShopSettleDetailRewardModeBO> detailBOList = new ArrayList<>();

        List<ShopSettleDetailDO> settleDetailDOList = settleShopDetailMapper.selectShopSettleDetail(param);
        if(!settleDetailDOList.isEmpty()){
            Set<String> insuranceOrderSns = new HashSet<>();
            Map<String, ShopSettleDetailRewardModeBO> map = new HashMap<>();
            //查询统计数据
            ShopSettleStatisticsDO statisticsDO = settleShopDetailMapper.selectShopSettleDetailStatistics(param);
            for(ShopSettleDetailDO settleDetailDO : settleDetailDOList){
                ShopSettleDetailRewardModeBO detailBO = BdUtil.do2bo(settleDetailDO, ShopSettleDetailRewardModeBO.class);
                detailBO.setAgentTagName(AgentTagEnum.codeDescription(settleDetailDO.getAgentTag()));
                detailBO.setInsuranceType(insuranceDicBiz.getInsuranceTypeNameByDicId(settleDetailDO.getInsuranceTypeId()));
                detailBO.setBalanceStatusName(insuranceDicBiz.getBalanceStatusNameByDicId(settleDetailDO.getBalanceStatus()));
                detailBO.setRewardStatusName(insuranceDicBiz.getRewardStatusNameByDicId(settleDetailDO.getRewardStatus()));

                //设置统计数据
                detailBO.setTotalInsuredFee(statisticsDO.getTotalInsuredFee());
                detailBO.setTotalSettleFee(statisticsDO.getTotalSettleFee());
                detailBO.setTotalCouponAmount(statisticsDO.getTotalCouponAmount());

                detailBOList.add(detailBO);

                //用过现金券的数据（目前只有商业险返利可以用券）
                if(YesNoEnum.YES.getCode().equals(detailBO.getIfUseCoupon()) && detailBO.getCouponNumber()>0) {
                    insuranceOrderSns.add(detailBO.getInsuranceOrderSn());
                    map.put(detailBO.getInsuranceOrderSn(), detailBO);
                }
            }

            total = settleShopDetailMapper.selectShopSettleDetailCount(param);

            //组装券号
            packageCouponInfo(map, insuranceOrderSns);
        }

        return PagingResult.wrapSuccessfulResult(detailBOList, total);
    }
    //组装券号
    private void packageCouponInfo(Map<String, ShopSettleDetailRewardModeBO> map, Set<String> insuranceOrderSns){
        if(insuranceOrderSns.isEmpty()){
            return;
        }
        SettleShopCheckDetailQueryPO queryPO = new SettleShopCheckDetailQueryPO();
        queryPO.setInsuranceOrderSnList(new ArrayList<>(insuranceOrderSns));
        queryPO.setSettleProjectId(SettleProjectEnum.CASH_COUPON.getCode());
        List<SettleShopCheckDetailDO> list = shopCheckDetailDOMapper.selectByCondition(queryPO);
        if(list.isEmpty()){
            return;
        }
        for(SettleShopCheckDetailDO couponRebate : list){
            ShopSettleDetailRewardModeBO rewardModeBO = map.get(couponRebate.getInsuranceOrderSn());
            if(rewardModeBO!=null){
                rewardModeBO.setCouponSn(couponRebate.getBizSn());
            }
        }
    }

    @Override
    public PagingResult<ShopSettleDetailPackageModeBO> shopSettleDetailPackageModePage(ShopSettleDetailQueryPO param) {
        Assert.notNull(param, "参数不能为空");
        Assert.notNull(param.getCooperationModeId(), "保险模式不能为空");
        //处理分页参数
        param.handlePageParam();
        //现金
        param.setSettleRuleType(SettleRuleTypeEnum.CASH.getCode());

        int total = 0;
        List<ShopSettleDetailPackageModeBO> detailExtBOList = new ArrayList<>();

        List<ShopSettleDetailDO> settleDetailDOList = settleShopDetailMapper.selectSettleDataForPackageMode(param);
        if(!settleDetailDOList.isEmpty()){
            //查询统计数据：现金
            ShopSettleStatisticsDO statisticsDO = settleShopDetailMapper.selectSettleDataForCashStatistics(param);
            //查询统计数据：服务包工时费
            ShopSettleStatisticsDO packageStatistics = settleShopDetailMapper.selectSettleDataForPackageStatistics(param);

            //商业险，组装服务包返利信息
//            Map<String, ShopSettleDetailPackageModeBO> bizRebateMap = new HashMap<>();
//            List<String> insuranceOrderSnList = new ArrayList<>();

            for(ShopSettleDetailDO settleDetailDO : settleDetailDOList){
                ShopSettleDetailPackageModeBO detailExtBO = BdUtil.do2bo(settleDetailDO, ShopSettleDetailPackageModeBO.class);
                detailExtBO.setAgentTagName(AgentTagEnum.codeDescription(settleDetailDO.getAgentTag()));
                detailExtBO.setInsuranceType(insuranceDicBiz.getInsuranceTypeNameByDicId(settleDetailDO.getInsuranceTypeId()));
                detailExtBO.setBalanceStatusName(insuranceDicBiz.getBalanceStatusNameByDicId(settleDetailDO.getBalanceStatus()));

                //设置 settle_company_check_detail 相关的信息
                setCompanyCheckInfo(detailExtBO, settleDetailDO);

                //设置统计数据
                detailExtBO.setTotalInsuredFee(statisticsDO.getTotalInsuredFee());
                detailExtBO.setTotalCash(statisticsDO.getTotalCash());
                detailExtBO.setTotalServiceFee(packageStatistics.getTotalServiceFee());

                detailExtBOList.add(detailExtBO);

                //提取出商业险记录
//                if(SettleProjectEnum.BIZ_REBATE.getCode().equals(settleDetailDO.getSettleProjectId())){
//                    String insuranceOrderSn = settleDetailDO.getInsuranceOrderSn();
//                    bizRebateMap.put(insuranceOrderSn, detailExtBO);
//                    insuranceOrderSnList.add(insuranceOrderSn);
//                }

                //服务包返利不展示结算金额，只展示服务包工时费即可
                if(SettleProjectEnum.PACKAGE_REBATE.getCode().equals(settleDetailDO.getSettleProjectId())){
                    detailExtBO.setSettleFee(null);
                }
            }

            total = settleShopDetailMapper.selectSettleDataForPackageModeCount(param);

            //组装商业险+服务包返利信息
//            setServicePackageInfo(bizRebateMap, insuranceOrderSnList);
        }

        return PagingResult.wrapSuccessfulResult(detailExtBOList, total);
    }
    /** 设置 settle_company_check_detail 相关的信息 */
    private void setCompanyCheckInfo(ShopSettleDetailPackageModeBO detailExtBO, ShopSettleDetailDO settleDetailDO){
        Integer confirmMoneyStatus = settleDetailDO.getConfirmMoneyStatus();
        if(confirmMoneyStatus != null){
            if(confirmMoneyStatus==1){
                detailExtBO.setConfirmMoneyStatusName("已收款");
            }else{
                detailExtBO.setConfirmMoneyStatusName("未收款");
            }
        }

//        String insuredFormNo = detailExtBO.getInsuredFormNo();
//        if(StringUtils.isEmpty(insuredFormNo)){
//            return;
//        }
//        SettleCompanyCheckDetailDO companyCheckDetailDO = settleCompanyCheckDetailDOMapper.selectByInsuredFormNo(insuredFormNo);
//        if(companyCheckDetailDO != null){
//            if(companyCheckDetailDO.getConfirmMoneyStatus()){
//                detailExtBO.setConfirmMoneyStatusName("已收款");
//                detailExtBO.setConfirmMoneyTime(companyCheckDetailDO.getConfirmMoneyTime());
//            }else{
//                detailExtBO.setConfirmMoneyStatusName("未收款");
//            }
//        }
    }
    /** 商业险，组装服务包信息（mark：目前就一个服务包，所以这样做没有问题）*/
    private void setServicePackageInfo(Map<String, ShopSettleDetailPackageModeBO> bizRebateMap, List<String> insuranceOrderSnList){
        if(insuranceOrderSnList.isEmpty()){
            return;
        }
        List<SettleServiceCheckDetailDO> list = settleServiceCheckDetailDOMapper.selectByInsuranceOrderSnList(insuranceOrderSnList);
        if(list.isEmpty()){
            return;
        }
        for(SettleServiceCheckDetailDO detailDO : list){
            String insuranceOrderSn = detailDO.getInsuranceOrderSn();
            ShopSettleDetailPackageModeBO settleDetailExtBO = bizRebateMap.get(insuranceOrderSn);
            if(settleDetailExtBO==null){
                continue;
            }
            settleDetailExtBO.setSettlePackageFee(detailDO.getSettlePackageFee());
            settleDetailExtBO.setSettlePackageName(detailDO.getSettlePackageName());
            settleDetailExtBO.setSettlePackagePrice(detailDO.getSettlePackagePrice());
            settleDetailExtBO.setGmtFirstPaid(detailDO.getGmtFirstPaid());
            settleDetailExtBO.setSettlePackageOrderSn(detailDO.getSettlePackageOrderSn());
        }
    }


    @Transactional
    @Override
    public boolean modifyBalanceStatus(BalanceStatusModifyPO modifyPO) {
        log.info("modifyBalanceStatus param:{}", JsonUtil.objectToStr(modifyPO));
        Assert.notNull(modifyPO, "参数不能为空");

        String operator = modifyPO.getOperator();
        List<Integer> idList = modifyPO.getIdList();
        Integer status = modifyPO.getStatus();

        Assert.hasLength(operator, "操作人不能为空");
        Assert.notEmpty(idList, "数据id集合不能为空");
        Assert.isTrue(BalanceStatusEnum.isLegalCode(status)
                        && !BalanceStatusEnum.NOT_APPLY.getCode().equals(status), "不合法的状态，status："+status);

        SettleShopCheckDetailQueryPO queryPO = new SettleShopCheckDetailQueryPO();
        queryPO.setIdList(idList);
        List<SettleShopCheckDetailDO> shopCheckDetailDOList = shopCheckDetailDOMapper.selectByCondition(queryPO);
        Assert.notEmpty(shopCheckDetailDOList, "未查到数据，id集合："+idList.toString());

        //已打款奖励金数据
        final List<SettleShopCheckDetailDO> paidRewardList = new ArrayList<>();
        //商业险返利，淘汽保单号map
        Map<String, SettleShopCheckDetailDO> insuranceOrderSnMap = new HashMap<>();
        //需要操作的结算数据
        List<SettleShopCheckDetailDO> updateDOList = handleShopCheckDetail(shopCheckDetailDOList, status, operator, paidRewardList);
        for(SettleShopCheckDetailDO detailDO : updateDOList){
            shopCheckDetailDOMapper.updateByPrimaryKeySelective(detailDO);
            //商业险返利，需要关联操作其他返利数据
            if(SettleProjectEnum.BIZ_REBATE.getCode().equals(detailDO.getSettleProjectId())){
                insuranceOrderSnMap.put(detailDO.getInsuranceOrderSn(), detailDO);
            }
        }
        //商业险返利，关联操作其他返利数据
        bizRebateAssociationOperation(insuranceOrderSnMap);

        //奖励金确认收款，通知finance
        if(!paidRewardList.isEmpty()) {
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    rewardConfirmPaid(paidRewardList);
                }
            });
        }

        return true;
    }
    private String illegalStatusMsg(Integer id, Integer oldStatus, String msg, Integer status){
        StringBuilder sb = new StringBuilder();
        sb.append(msg).append("，状态不合法，数据状态：").append(oldStatus);
        sb.append(" [").append(BalanceStatusEnum.codeDescription(oldStatus)).append("]");
        sb.append("，操作状态：").append(status);
        sb.append(" [").append(BalanceStatusEnum.codeDescription(status)).append("]");
        sb.append("，数据id：").append(id);
        return sb.toString();
    }
    private List<SettleShopCheckDetailDO> handleShopCheckDetail(List<SettleShopCheckDetailDO> shopCheckDetailDOList,
                                                                Integer status, String operator,
                                                                List<SettleShopCheckDetailDO> paidRewardList){
        List<SettleShopCheckDetailDO> list = new ArrayList<>();
        if(BalanceStatusEnum.APPLYING.getCode().equals(status)){
            list = getApplyDataList(shopCheckDetailDOList, status, operator);
        }else if(BalanceStatusEnum.AUDIT_SUCCESS.getCode().equals(status)
                || BalanceStatusEnum.AUDIT_FAILED.getCode().equals(status)){
            list = getAuditDataList(shopCheckDetailDOList, status, operator);
        }else if(BalanceStatusEnum.CONFIRM_PAID.getCode().equals(status)){
            list = getPayDataList(shopCheckDetailDOList, status, operator, paidRewardList);
        }
        return list;
    }
    private SettleShopCheckDetailDO getUpdateDO(SettleShopCheckDetailDO shopCheckDetailDO,
                                                Integer status, String operator, Date now){
        SettleShopCheckDetailDO updateDO = new SettleShopCheckDetailDO();
        updateDO.setId(shopCheckDetailDO.getId());
        updateDO.setBalanceStatus(status);
        updateDO.setModifier(operator);
        updateDO.setGmtModified(now);
        //用于后续的判断逻辑
        updateDO.setSettleProjectId(shopCheckDetailDO.getSettleProjectId());
        updateDO.setInsuranceOrderSn(shopCheckDetailDO.getInsuranceOrderSn());
        return updateDO;
    }
    private List<SettleShopCheckDetailDO> getApplyDataList(List<SettleShopCheckDetailDO> shopCheckDetailDOList,
                                                           Integer status, String operator){
        Date now = new Date();
        List<SettleShopCheckDetailDO> list = new ArrayList<>();
        for(SettleShopCheckDetailDO shopCheckDetailDO : shopCheckDetailDOList){
            Integer id = shopCheckDetailDO.getId();
            Integer oldStatus = shopCheckDetailDO.getBalanceStatus();
            Assert.isTrue(BalanceStatusEnum.canApplying(oldStatus), illegalStatusMsg(id, oldStatus, "申请失败", status));

            SettleShopCheckDetailDO updateDO = getUpdateDO(shopCheckDetailDO, status, operator, now);
            updateDO.setApplyPeopleName(operator);
            updateDO.setApplyTime(now);
            list.add(updateDO);
        }
        return list;
    }
    private List<SettleShopCheckDetailDO> getAuditDataList(List<SettleShopCheckDetailDO> shopCheckDetailDOList,
                                                           Integer status, String operator){
        Date now = new Date();
        List<SettleShopCheckDetailDO> list = new ArrayList<>();
        for(SettleShopCheckDetailDO shopCheckDetailDO : shopCheckDetailDOList){
            Integer id = shopCheckDetailDO.getId();
            Integer oldStatus = shopCheckDetailDO.getBalanceStatus();
            Assert.isTrue(BalanceStatusEnum.canAudit(oldStatus), illegalStatusMsg(id, oldStatus, "审核失败", status));

            SettleShopCheckDetailDO updateDO = getUpdateDO(shopCheckDetailDO, status, operator, now);
            updateDO.setAuditPeopleName(operator);
            updateDO.setAuditTime(now);
            list.add(updateDO);
        }
        return list;
    }
    private List<SettleShopCheckDetailDO> getPayDataList(List<SettleShopCheckDetailDO> shopCheckDetailDOList,
                                                         Integer status, String operator,
                                                         List<SettleShopCheckDetailDO> paidRewardList){
        Date now = new Date();
        List<SettleShopCheckDetailDO> list = new ArrayList<>();
        for(SettleShopCheckDetailDO shopCheckDetailDO : shopCheckDetailDOList){
            Integer id = shopCheckDetailDO.getId();
            Integer oldStatus = shopCheckDetailDO.getBalanceStatus();
            Assert.isTrue(BalanceStatusEnum.canPay(oldStatus), illegalStatusMsg(id, oldStatus, "确认打款失败", status));

            SettleShopCheckDetailDO updateDO = getUpdateDO(shopCheckDetailDO, status, operator, now);
            updateDO.setSettlePeopleName(operator);
            updateDO.setSettleTime(now);
            updateDO.setSettleFeeStatus(SettleStatusEnum.HAS_PAID.getCode());
            list.add(updateDO);

            //奖励金提现
            if(CooperationModeEnum.GIVE_REWARD.getCode().equals(shopCheckDetailDO.getCooperationModeId())){
                paidRewardList.add(shopCheckDetailDO);
            }
        }
        return list;
    }
    //商业险返利，需要关联操作其他返利数据
    private void bizRebateAssociationOperation(Map<String, SettleShopCheckDetailDO> insuranceOrderSnMap){
        if(insuranceOrderSnMap.isEmpty()){
            return;
        }
        List<String> insuranceOrderSnList = new ArrayList<>(insuranceOrderSnMap.keySet());
        //操作服务包返利
        SettleShopCheckDetailQueryPO queryPO = new SettleShopCheckDetailQueryPO();
        queryPO.setInsuranceOrderSnList(insuranceOrderSnList);
        queryPO.setSettleProjectId(SettleProjectEnum.PACKAGE_REBATE.getCode());
        List<SettleShopCheckDetailDO> checkDetailDOList = shopCheckDetailDOMapper.selectByCondition(queryPO);
        for(SettleShopCheckDetailDO checkDetailDO : checkDetailDOList){
            SettleShopCheckDetailDO updateDO = getDetailDOForAssociationOperation(insuranceOrderSnMap, checkDetailDO);
            shopCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);
        }

        //操作现金券结算数据
        List<String> settleCouponSnList = new ArrayList<>();
        queryPO.setSettleProjectId(SettleProjectEnum.CASH_COUPON.getCode());
        checkDetailDOList = shopCheckDetailDOMapper.selectByCondition(queryPO);
        for(SettleShopCheckDetailDO checkDetailDO : checkDetailDOList){
            SettleShopCheckDetailDO updateDO = getDetailDOForAssociationOperation(insuranceOrderSnMap, checkDetailDO);
            shopCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);
            //确认打款
            if(BalanceStatusEnum.CONFIRM_PAID.getCode().equals(updateDO.getBalanceStatus())) {
                settleCouponSnList.add(checkDetailDO.getBizSn());
            }
        }

        // 操作insurance现金券
        extCashCouponService.settleCashCoupon(settleCouponSnList);
    }
    private SettleShopCheckDetailDO getDetailDOForAssociationOperation(Map<String, SettleShopCheckDetailDO> insuranceOrderSnMap,
                                                                       SettleShopCheckDetailDO shopCheckDetailDO){
        //商业险返利改动数据
        SettleShopCheckDetailDO bizRebateUpdateDO = insuranceOrderSnMap.get(shopCheckDetailDO.getInsuranceOrderSn());
        //需要关联操作的数据
        SettleShopCheckDetailDO updateDO = new SettleShopCheckDetailDO();
        updateDO.setId(shopCheckDetailDO.getId());
        updateDO.setSettleFeeStatus(bizRebateUpdateDO.getSettleFeeStatus());
        updateDO.setBalanceStatus(bizRebateUpdateDO.getBalanceStatus());
        updateDO.setModifier(bizRebateUpdateDO.getModifier());
        updateDO.setGmtModified(bizRebateUpdateDO.getGmtModified());
        updateDO.setApplyTime(bizRebateUpdateDO.getApplyTime());
        updateDO.setApplyPeopleName(bizRebateUpdateDO.getApplyPeopleName());
        updateDO.setAuditTime(bizRebateUpdateDO.getAuditTime());
        updateDO.setAuditPeopleName(bizRebateUpdateDO.getAuditPeopleName());
        updateDO.setSettleTime(bizRebateUpdateDO.getSettleTime());
        updateDO.setSettlePeopleName(bizRebateUpdateDO.getSettlePeopleName());
        return updateDO;
    }
    //奖励金确认收款，通知finance
    private void rewardConfirmPaid(List<SettleShopCheckDetailDO> detailDOList){
        for(SettleShopCheckDetailDO detailDO : detailDOList) {
            WithdrawRewardPO withdrawRewardPO = new WithdrawRewardPO();
            withdrawRewardPO.setShopId(detailDO.getAgentId());
            withdrawRewardPO.setInsuranceNo(detailDO.getBizSn());
            withdrawRewardPO.setWithdrawAmount(detailDO.getSettleFee());
            extFcInsuranceService.withdrawReward(withdrawRewardPO);
        }
    }



    @Transactional
    @Override
    public boolean unUseCashCoupon(SettleFeeModifyPO modifyPO) {
        Assert.notNull(modifyPO, "参数不能为空");
        String insuranceOrderSn = modifyPO.getInsuranceOrderSn();
        Assert.hasLength(insuranceOrderSn, "淘汽保单号不能为空");
        BigDecimal bizInsuranceRate = modifyPO.getBizInsuranceRate();
        Assert.isTrue(bizInsuranceRate!=null && bizInsuranceRate.compareTo(BigDecimal.ZERO)>-1, "商业险返利比例不能为空");
        BigDecimal forceInsuranceRate = modifyPO.getForceInsuranceRate();
        Assert.isTrue(forceInsuranceRate!=null && forceInsuranceRate.compareTo(BigDecimal.ZERO)>-1, "交强险返利比例不能为空");

        //校验保单
        List<String> insuranceOrderSnList = new ArrayList<>();
        insuranceOrderSnList.add(insuranceOrderSn);
        List<SettleFormDO> formDOList = settleFormDOMapper.getSettleFormListByInsuranceOrderSn(insuranceOrderSnList);
        Assert.notEmpty(formDOList, "未查到保单数据，淘汽保单号："+insuranceOrderSn);
        //商业险保单
        SettleFormDO bizForm = getSettleForm(InsuranceTypeEnum.BIZ_INSURANCE.getCode(), formDOList);
        Assert.notNull(bizForm, "未查到商业险保单，淘汽保单号："+insuranceOrderSn);
        //交强险保单
        SettleFormDO forceForm = getSettleForm(InsuranceTypeEnum.FORCE_INSURANCE.getCode(), formDOList);
//        Assert.notNull(forceForm, "未查到交强险保单，淘汽保单号："+insuranceOrderSn);

        //校验返利
        SettleShopCheckDetailQueryPO queryPO = new SettleShopCheckDetailQueryPO();
        queryPO.setInsuranceOrderSnList(insuranceOrderSnList);
        List<SettleShopCheckDetailDO> checkDetailDOList = shopCheckDetailDOMapper.selectByCondition(queryPO);
        Assert.notEmpty(checkDetailDOList, "未查到保单返利，淘汽保单号："+insuranceOrderSn);

        //现金券兑现
        SettleShopCheckDetailDO cashCoupon = getShopCheckDetail(SettleProjectEnum.CASH_COUPON.getCode(), checkDetailDOList);
        Assert.notNull(cashCoupon, "未查到现金券兑现记录，淘汽保单号："+insuranceOrderSn);
        //商业险返利
        SettleShopCheckDetailDO bizRebate = getShopCheckDetail(SettleProjectEnum.BIZ_REBATE.getCode(), checkDetailDOList);
        Assert.notNull(bizRebate, "未查到商业险返利，淘汽保单号："+insuranceOrderSn);
        //交强险返利
        SettleShopCheckDetailDO forceRebate = getShopCheckDetail(SettleProjectEnum.FORCE_REBATE.getCode(), checkDetailDOList);
//        Assert.notNull(forceRebate, "未查到交强险返利，淘汽保单号："+insuranceOrderSn);

        //删除现金券兑现记录
        SettleShopCheckDetailDO updateDO = new SettleShopCheckDetailDO();
        updateDO.setId(cashCoupon.getId());
        updateDO.setGmtModified(new Date());
        updateDO.setIsDeleted("Y");
        shopCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);

        //修改商业险返利
        BigDecimal rebate = bizForm.getInsuredFee().multiply(bizInsuranceRate);
        updateDO = getRebateDOForUnUseCashCoupon(bizRebate, bizInsuranceRate, rebate);
        shopCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);

        //修改交强险返利
        if(forceForm!=null && forceRebate!=null) {
            rebate = forceForm.getInsuredFee().multiply(forceInsuranceRate);
            updateDO = getRebateDOForUnUseCashCoupon(forceRebate, forceInsuranceRate, rebate);
            shopCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);
        }

        return true;
    }
    private SettleShopCheckDetailDO getRebateDOForUnUseCashCoupon(SettleShopCheckDetailDO detailDO,
                                                                  BigDecimal settleRate, BigDecimal settleFee){
        SettleShopCheckDetailDO updateDO = new SettleShopCheckDetailDO();
        updateDO.setId(detailDO.getId());
        updateDO.setIfUseCoupon(0);
        updateDO.setCouponNumber(0);
        updateDO.setCouponAmount(BigDecimal.ZERO);
        updateDO.setSettleBaseAmount(BigDecimal.ZERO);
        updateDO.setSettleRate(ManaUtil.getDecimalHalfUp(settleRate));
        updateDO.setSettleFee(ManaUtil.getDecimalHalfUp(settleFee));
        return updateDO;
    }

    private SettleFormDO getSettleForm(Integer type, List<SettleFormDO> formDOList){
        for(SettleFormDO formDO : formDOList){
            if (type.equals(formDO.getInsuranceTypeId())){
                return formDO;
            }
        }
        return null;
    }
    private SettleShopCheckDetailDO getShopCheckDetail(Integer projectId, List<SettleShopCheckDetailDO> checkDetailDOList){
        for(SettleShopCheckDetailDO checkDetailDO : checkDetailDOList){
            if(projectId.equals(checkDetailDO.getSettleProjectId())){
                return checkDetailDO;
            }
        }
        return null;
    }

    private void checkUseCashCouponParam(UseCashCouponPO useCashCouponPO){
        Assert.notNull(useCashCouponPO, "参数不能为空");
        Assert.notNull(useCashCouponPO.getInsuranceCompanyId(), "保险公司id不能为空");
        Assert.hasLength(useCashCouponPO.getInsuranceOrderSn(), "淘汽保单号不能为空");
        Assert.hasLength(useCashCouponPO.getCityCode(), "城市编码不能为空");
        Assert.hasLength(useCashCouponPO.getCouponSn(), "现金券编号不能为空");
        Assert.notNull(useCashCouponPO.getCouponAmount(), "现金券金额不能为空");
        Assert.notNull(useCashCouponPO.getCouponCashTime(), "现金券兑现时间不能为空");
        Assert.isTrue(!(StringUtils.isEmpty(useCashCouponPO.getBizInsuranceFormulaKey())
                && StringUtils.isEmpty(useCashCouponPO.getForceInsuranceFormulaKey())), "返利计算公式key不能为空");
    }
    @Transactional
    @Override
    public boolean useCashCoupon(UseCashCouponPO useCashCouponPO) {
        log.info("use cash coupon, param:{}", JsonUtil.objectToStr(useCashCouponPO));
        checkUseCashCouponParam(useCashCouponPO);

        String insuranceOrderSn = useCashCouponPO.getInsuranceOrderSn();
        Integer insuranceCompanyId = useCashCouponPO.getInsuranceCompanyId();
        //校验保单
        List<String> insuranceOrderSnList = new ArrayList<>();
        insuranceOrderSnList.add(insuranceOrderSn);
        List<SettleFormDO> formDOList = settleFormDOMapper.getSettleFormListByInsuranceOrderSn(insuranceOrderSnList);
        Assert.notEmpty(formDOList, "未查到保单数据，淘汽保单号：" + insuranceOrderSn);
        //todo 使用现金券，必须有商业险
        SettleFormDO bizForm = getSettleForm(InsuranceTypeEnum.BIZ_INSURANCE.getCode(), formDOList);
        Assert.notNull(bizForm, "未查到商业险保单，淘汽保单号：" + insuranceOrderSn);

        //校验保单返利
        List<String> formSnList = new ArrayList<>();
        for(SettleFormDO formDO : formDOList){
            formSnList.add(formDO.getInsuredFormNo());
        }
        SettleShopCheckDetailQueryPO queryPO = new SettleShopCheckDetailQueryPO();
        queryPO.setBizSnList(formSnList);
        queryPO.setInsuranceCompanyId(insuranceCompanyId);
        List<SettleShopCheckDetailDO> checkDetailDOList = shopCheckDetailDOMapper.selectByCondition(queryPO);
        Assert.notEmpty(checkDetailDOList, "未查到保单返利，淘汽保单号：" + insuranceOrderSn);
        //todo 使用现金券，必须有商业险
        SettleShopCheckDetailDO bizCheckDetail = getShopCheckDetail(SettleProjectEnum.BIZ_REBATE.getCode(), checkDetailDOList);
        Assert.notNull(bizCheckDetail, "未查到商业险返利，淘汽保单号："+insuranceOrderSn);

        for(SettleShopCheckDetailDO checkDetailDO : checkDetailDOList){
            Assert.isTrue(!SettleStatusEnum.HAS_PAID.getCode().equals(checkDetailDO.getSettleFeeStatus()),
                    "返利已结算，不能修改结算金额，淘汽保单号："+insuranceOrderSn);
            Assert.isTrue(YesNoEnum.NO.getCode().equals(checkDetailDO.getIfUseCoupon()),
                    "不能重复用券，淘汽保单号："+insuranceOrderSn);
        }

        // 返利比例（从新的配置表获取）
        String cityCode = useCashCouponPO.getCityCode();
        CashCouponRuleConfigBO couponRuleConfigBO = getCashCouponRuleConfig(cityCode);
        Assert.notNull(couponRuleConfigBO, "未配置返利比例，城市编码："+cityCode);

//        RateConfigQueryPO rateConfigQueryPO = new RateConfigQueryPO();
//        rateConfigQueryPO.setInsuranceCompanyId(insuranceCompanyId);
//        rateConfigQueryPO.setCityCode(useCashCouponPO.getCityCode());
//        List<SettleRateConfigDO> rateConfigDOList = rateConfigBiz.getAll(rateConfigQueryPO);
//        Assert.notEmpty(rateConfigDOList, "未配置返利比例，保险公司id："+insuranceCompanyId);

        //修改商业险返利
        SettleShopCheckDetailDO checkDetailDO = toModifyRebate(formDOList, checkDetailDOList,
                couponRuleConfigBO.getCommercialRebateRatio(),
                InsuranceTypeEnum.BIZ_INSURANCE, useCashCouponPO.getCouponAmount(), 1,
                useCashCouponPO.getBizInsuranceFormulaKey());

        //修改交强险返利
        toModifyRebate(formDOList, checkDetailDOList, couponRuleConfigBO.getForcibleRebateRatio(),
                InsuranceTypeEnum.FORCE_INSURANCE, null, null, useCashCouponPO.getForceInsuranceFormulaKey());

        //插入现金券结算数据
        addCouponSettleData(useCashCouponPO, checkDetailDO);

        log.info("use cash coupon, insuranceOrderSn:{}, old data:{}", insuranceOrderSn, JsonUtil.objectToStr(checkDetailDOList));

        return true;
    }
    //获取返利比例配置
    private CashCouponRuleConfigBO getCashCouponRuleConfig(String cityCode){
        try {
            return cashCouponRuleConfigBiz.getConfigInfoByCityCode(cityCode);
        }catch (Exception e){
            log.error("getCashCouponRuleConfig failed, cityCode:"+cityCode, e);
        }
        return null;
    }
    /** 修改返利数据 */
    private SettleShopCheckDetailDO toModifyRebate(
            List<SettleFormDO> formDOList, List<SettleShopCheckDetailDO> checkDetailDOList,
            BigDecimal rate, InsuranceTypeEnum insuranceTypeEnum,
            BigDecimal couponAmount, Integer couponNumber, String formulaKey){

        if(StringUtils.isEmpty(formulaKey)){
            return null;
        }
        //保单
        SettleFormDO formDO = getSettleForm(insuranceTypeEnum.getCode(), formDOList);
        if(formDO==null){
            return null;
        }

        Integer settleProject;
//        Integer rateType;
        String insuredFeeVariable; //保费变量
        String rebateRateVariable; //返利比例变量
        if(InsuranceTypeEnum.FORCE_INSURANCE.equals(insuranceTypeEnum)){
            settleProject = SettleProjectEnum.FORCE_REBATE.getCode();
//            rateType = RateTypeEnum.FORCE_INSURANCE.getCode();
            insuredFeeVariable = FormulaVariableEnum.FORCE_INSURANCE_FEE.getKey();
            rebateRateVariable = FormulaVariableEnum.FORCE_INSURANCE_RATE.getKey();
        }else{
            settleProject = SettleProjectEnum.BIZ_REBATE.getCode();
//            rateType = RateTypeEnum.BIZ_INSURANCE.getCode();
            insuredFeeVariable = FormulaVariableEnum.BIZ_INSURANCE_FEE.getKey();
            rebateRateVariable = FormulaVariableEnum.BIZ_INSURANCE_RATE.getKey();
        }

        //返利
        SettleShopCheckDetailDO checkDetailDO = getShopCheckDetail(settleProject, checkDetailDOList);
        if(checkDetailDO==null){
            return null;
        }

        String insuranceTypeName = insuranceTypeEnum.getDesc();
        Assert.notNull(rate, "未配置"+insuranceTypeName+"返利比例");
        //返利公式
        SettleFeeFormulaConfigBO formulaConfigBO = formulaConfigBiz.getFormulaConfigByKey(formulaKey);
        Assert.notNull(formulaConfigBO, "未配置"+insuranceTypeName+"返利计算公式");

        //公式变量处理
        Evaluator evaluator = new Evaluator();
//        BigDecimal rate = null; //比例
//        for(SettleRateConfigDO rateConfigDO : rateConfigDOList){
//            if(rateType.equals(rateConfigDO.getRateType())){
//                rate = rateConfigDO.getRateValue();
//                evaluator.putVariable(rateConfigDO.getRateKey(), rate.toString());
//                break;
//            }
//        }

        evaluator.putVariable(rebateRateVariable, rate.toString());
        evaluator.putVariable(insuredFeeVariable, formDO.getInsuredFee().toString());
        if(couponAmount!=null){
            evaluator.putVariable(FormulaVariableEnum.COUPON_AMOUNT.getKey(), couponAmount.toString());
        }

        //修改返利
        modifyRebate(formulaConfigBO, evaluator, rate, checkDetailDO, couponAmount, couponNumber);

        return checkDetailDO;
    }
    private void modifyRebate(SettleFeeFormulaConfigBO formulaConfigBO, Evaluator evaluator, BigDecimal rate,
                              SettleShopCheckDetailDO checkDetailDO, BigDecimal couponAmount, Integer couponNumber){
        //公式
        String exp = formulaConfigBO.getFormulaExpress();
        exp = ManaUtil.formatExpForJEval(exp);
        try {
//            String fun = evaluator.replaceVariables(exp); //替换变量后的公式

            BigDecimal result = new BigDecimal(evaluator.evaluate(exp));

            SettleShopCheckDetailDO updateDO = new SettleShopCheckDetailDO();
            updateDO.setId(checkDetailDO.getId());
            updateDO.setSettleFee(ManaUtil.getDecimalHalfUp(result));
            updateDO.setSettleRate(rate);
            updateDO.setGmtModified(new Date());
            updateDO.setIfUseCoupon(YesNoEnum.YES.getCode());
            updateDO.setCouponAmount(couponAmount);
            updateDO.setCouponNumber(couponNumber);

            if(rate.doubleValue()>0) {
                BigDecimal settleBaseAmount = result.divide(rate, 2, RoundingMode.HALF_UP);
                updateDO.setSettleBaseAmount(settleBaseAmount);
            }

            shopCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);

        } catch (EvaluationException e) {
            log.error("settle fee evaluate error, bizSn:"+checkDetailDO.getBizSn()+", formula:"+exp, e);
            throw new BusinessException(e.getMessage());
        }
    }

    /** 插入现金券结算记录 */
    private void addCouponSettleData(UseCashCouponPO useCashCouponPO, SettleShopCheckDetailDO checkDetailDO){
        SettleShopCheckDetailDO detailDO = new SettleShopCheckDetailDO();
        detailDO.setGmtCreate(new Date());
        detailDO.setSettleRuleType(SettleRuleTypeEnum.CASH.getCode());
        detailDO.setSettleProjectId(SettleProjectEnum.CASH_COUPON.getCode());
        detailDO.setSettleProjectName(SettleProjectEnum.CASH_COUPON.getDesc());
        detailDO.setSettleConditionId(SettleConditionEnum.COUPON_CASH_TIME.getCode());
        detailDO.setSettleConditionName(SettleConditionEnum.COUPON_CASH_TIME.getDesc());

        detailDO.setSettleConditionTime(useCashCouponPO.getCouponCashTime());
        detailDO.setBizSn(useCashCouponPO.getCouponSn());
        detailDO.setInsuranceOrderSn(useCashCouponPO.getInsuranceOrderSn());
        detailDO.setSettleFee(useCashCouponPO.getCouponAmount());

        detailDO.setAgentId(checkDetailDO.getAgentId());
        detailDO.setAgentName(checkDetailDO.getAgentName());
        detailDO.setAgentAccount(checkDetailDO.getAgentAccount());
        detailDO.setCooperationModeId(checkDetailDO.getCooperationModeId());
        detailDO.setCooperationModeName(checkDetailDO.getCooperationModeName());
        detailDO.setCarOwnerName(checkDetailDO.getCarOwnerName());
        detailDO.setVehicleSn(checkDetailDO.getVehicleSn());
        detailDO.setNewVehicleSn(checkDetailDO.getNewVehicleSn());
        detailDO.setInsuranceCompanyId(checkDetailDO.getInsuranceCompanyId());
        detailDO.setInsuranceCompanyName(checkDetailDO.getInsuranceCompanyName());

        shopCheckDetailDOMapper.insertSelective(detailDO);
    }

}
