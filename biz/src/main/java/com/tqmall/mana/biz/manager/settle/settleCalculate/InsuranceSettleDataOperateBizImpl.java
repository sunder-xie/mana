package com.tqmall.mana.biz.manager.settle.settleCalculate;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.entity.settle.*;
import com.tqmall.mana.beans.param.settle.SettleShopCheckDetailQueryPO;
import com.tqmall.mana.biz.manager.insurance.InsuranceBiz;
import com.tqmall.mana.biz.manager.insurance.InsuranceDicBiz;
import com.tqmall.mana.component.annotation.lock.ManaLockKey;
import com.tqmall.mana.component.bean.ConstantBean;
import com.tqmall.mana.component.enums.SettleDataOperateEnum;
import com.tqmall.mana.component.enums.YesNoEnum;
import com.tqmall.mana.component.enums.insurance.dict.*;
import com.tqmall.mana.dao.mapper.settle.*;
import com.tqmall.mana.external.dubbo.uc.ExtShopInfoService;
import com.tqmall.ucenter.object.result.account.AccountDTO;
import com.tqmall.ucenter.object.result.shoptag.ShopTagDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huangzhangting on 17/2/10.
 */
@Slf4j
@Service
public class InsuranceSettleDataOperateBizImpl implements InsuranceSettleDataOperateBiz {
    @Autowired
    private SettleServiceCheckDetailDOMapper serviceCheckDetailDOMapper;
    @Autowired
    private SettleFormDOMapper settleFormDOMapper;
    @Autowired
    private SettleShopCheckDetailDOMapper shopCheckDetailDOMapper;
    @Autowired
    private SettleCarOwnerCheckDetailDOMapper carOwnerCheckDetailDOMapper;
//    @Autowired
//    private SettleShopBaseBiz settleShopBaseBiz;
    @Autowired
    private InsuranceBiz insuranceBiz;
    @Autowired
    private InsuranceDicBiz insuranceDicBiz;
    @Autowired
    private ExtShopInfoService extShopInfoService;
    @Autowired
    private SettleShopCheckDetailExtendDOMapper shopCheckDetailExtendDOMapper;


    @Override
//    @ManaLock(lockKeyName = RedisKeyBean.LOCK_INSERT_SERVICE_CHECK_DETAIL)
    public Integer insertOrUpdateServiceCheckDetail(@ManaLockKey String insuranceOrderSn, SettleServiceCheckDetailDO checkDetail, Integer shopId) {
        if(checkDetail==null){
            return null;
        }
        //todo 优化，现在就一个服务包数据，这样设计没有问题，以后如果是多个服务包，就会有问题了
        //根据 insurance_order_sn 判断服务包记录是否存在
        List<String> insuranceOrderSnList = new ArrayList<>();
        insuranceOrderSnList.add(checkDetail.getInsuranceOrderSn());
        List<SettleServiceCheckDetailDO> serviceCheckDetailDOList = serviceCheckDetailDOMapper.selectByInsuranceOrderSnList(insuranceOrderSnList);
        if(!serviceCheckDetailDOList.isEmpty()){
            //买保险送服务包，保单生效（服务包生效）才会有物料订单号，需要把物料订单号更新到相关表
            String packageOrderSn = checkDetail.getSettlePackageOrderSn();
            if(!StringUtils.isEmpty(packageOrderSn)){
                SettleServiceCheckDetailDO serviceCheckDetailDO = serviceCheckDetailDOList.get(0);
                if(StringUtils.isEmpty(serviceCheckDetailDO.getSettlePackageOrderSn())){
                    SettleServiceCheckDetailDO updateDO = new SettleServiceCheckDetailDO();
                    updateDO.setId(serviceCheckDetailDO.getId());
                    updateDO.setSettlePackageOrderSn(packageOrderSn);
                    updateDO.setGmtModified(new Date());
                    if(ServicePackageStatusEnum.NOT_EFFECT.getCode().equals(serviceCheckDetailDO.getSettlePackageStatus())) {
                        updateDO.setSettlePackageStatus(ServicePackageStatusEnum.WAIT_SEND.getCode());

                        /** 新增待发货的服务包数量 */
//                        settleShopBaseBiz.changePackageWaitEffectToWait(1, shopId);
                    }
                    serviceCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);

                    //更新门店对账表的 biz_sn
                    SettleShopCheckDetailQueryPO queryPO = new SettleShopCheckDetailQueryPO();
                    queryPO.setInsuranceOrderSnList(insuranceOrderSnList);
                    queryPO.setSettleProjectId(SettleProjectEnum.PACKAGE_REBATE.getCode());
                    List<SettleShopCheckDetailDO> shopCheckDetailDOList = shopCheckDetailDOMapper.selectByCondition(queryPO);
                    if(!shopCheckDetailDOList.isEmpty()){
                        SettleShopCheckDetailDO shopCheckDetailDO = shopCheckDetailDOList.get(0);
                        SettleShopCheckDetailDO shopUpdateDO = new SettleShopCheckDetailDO();
                        shopUpdateDO.setId(shopCheckDetailDO.getId());
                        shopUpdateDO.setBizSn(packageOrderSn);
                        shopUpdateDO.setGmtModified(new Date());
                        shopCheckDetailDOMapper.updateByPrimaryKeySelective(shopUpdateDO);
                    }
                }
            }

            return SettleDataOperateEnum.UPDATE.getCode();
        }

        serviceCheckDetailDOMapper.insertSelective(checkDetail);
        return SettleDataOperateEnum.INSERT.getCode();
    }

    @Override
//    @ManaLock(lockKeyName = RedisKeyBean.LOCK_INSERT_SETTLE_FORM)
    public Integer insertOrUpdateSettleForm(@ManaLockKey String insuranceOrderSn, SettleFormDO settleForm) {
        if(settleForm==null){
            return null;
        }
        //根据 insured_form_no 判断保单记录是否存在
        Integer id = settleFormDOMapper.selectIdByFormNo(settleForm.getInsuredFormNo(), settleForm.getInsuranceCompanyId());
        if(id != null){
            //目前没有修改的需求
            return SettleDataOperateEnum.UPDATE.getCode();
        }

        //如果是买服务包送保险模式，需要更新车主对账表的支付状态
        if(CooperationModeEnum.GIVE_INSURANCE.getCode().equals(settleForm.getCooperationModeId())){
            List<String> orderSnList = new ArrayList<>();
            orderSnList.add(settleForm.getInsuranceOrderSn());
            List<SettleCarOwnerCheckDetailDO> carOwnerCheckDetailDOList =
                    carOwnerCheckDetailDOMapper.getCarOwnerCheckDetailForPageByInsuranceOrderSnList(orderSnList);
            if(!carOwnerCheckDetailDOList.isEmpty()){
                SettleCarOwnerCheckDetailDO carOwnerCheckDetailDO = carOwnerCheckDetailDOList.get(0);
                SettleCarOwnerCheckDetailDO updateDO = new SettleCarOwnerCheckDetailDO();
                updateDO.setId(carOwnerCheckDetailDO.getId());
                updateDO.setIsTqmallPayStatus(TqmallPayStatusEnum.HAS_PAID.getCode());
                carOwnerCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);
            }
        }

        settleFormDOMapper.insertSelective(settleForm);
        return SettleDataOperateEnum.INSERT.getCode();
    }

    @Override
//    @ManaLock(lockKeyName = RedisKeyBean.LOCK_INSERT_SHOP_CHECK_DETAIL)
    public Integer insertOrUpdateShopCheckDetail(@ManaLockKey String insuranceOrderSn, SettleShopCheckDetailDO checkDetail) {
        if(checkDetail==null){
            return null;
        }
        Integer flag = updateShopCheckDetail(checkDetail);
        if(flag!=null){
            return flag;
        }

        shopCheckDetailDOMapper.insertSelective(checkDetail);

        //如果是奖励金模式
        if(CooperationModeEnum.GIVE_REWARD.getCode().equals(checkDetail.getCooperationModeId())){
            SettleShopCheckDetailExtendDO extendDO = new SettleShopCheckDetailExtendDO();
            extendDO.setGmtCreate(new Date());
            extendDO.setShopCheckDetailId(checkDetail.getId());
            extendDO.setRewardStatus(RewardStatusEnum.NOT_EFFECT.getCode());
//            extendDO.setWithdrawCashStatus(WithdrawCashStatusEnum.NOT_APPLY.getCode());
            shopCheckDetailExtendDOMapper.insertSelective(extendDO);
        }

        return SettleDataOperateEnum.INSERT.getCode();
    }

    @Override
    public Integer updateShopCheckDetail(SettleShopCheckDetailDO checkDetail) {
        if(checkDetail==null){
            return null;
        }
        //根据 insurance_order_sn + settle_project_id 判断记录是否存在
        List<String> insuranceOrderSnList = new ArrayList<>();
        insuranceOrderSnList.add(checkDetail.getInsuranceOrderSn());
        SettleShopCheckDetailQueryPO queryPO = new SettleShopCheckDetailQueryPO();
        queryPO.setInsuranceOrderSnList(insuranceOrderSnList);
        queryPO.setSettleProjectId(checkDetail.getSettleProjectId());
        List<SettleShopCheckDetailDO> shopCheckDetailDOList = shopCheckDetailDOMapper.selectByCondition(queryPO);
        if(shopCheckDetailDOList.isEmpty()){
           return null;
        }
        SettleShopCheckDetailDO shopCheckDetailDO = shopCheckDetailDOList.get(0);
        //已操作过的数据不能改
        if(!BalanceStatusEnum.NOT_APPLY.getCode().equals(shopCheckDetailDO.getBalanceStatus())
                || !YesNoEnum.NO.getCode().equals(shopCheckDetailDO.getIfUseCoupon())){
            return SettleDataOperateEnum.UPDATE.getCode();
        }
        checkDetail.setId(shopCheckDetailDO.getId());
        //设置不能修改的字段
        checkDetail.setGmtCreate(null);
        checkDetail.setSettleFeeStatus(null);
        checkDetail.setBalanceStatus(null);
        checkDetail.setApplyPeopleName(null);
        checkDetail.setApplyTime(null);
        checkDetail.setAuditPeopleName(null);
        checkDetail.setAuditTime(null);
        checkDetail.setSettlePeopleName(null);
        checkDetail.setSettleTime(null);
        checkDetail.setIfUseCoupon(null);

        shopCheckDetailDOMapper.updateByPrimaryKeySelective(checkDetail);
        return SettleDataOperateEnum.UPDATE.getCode();
    }

    @Override
//    @ManaLock(lockKeyName = RedisKeyBean.LOCK_INSERT_CAR_OWNER_CHECK_DETAIL)
    public Integer insertOrUpdateCarOwnerCheckDetail(@ManaLockKey String insuranceOrderSn, SettleCarOwnerCheckDetailDO checkDetail) {
        if(checkDetail==null){
            return null;
        }
        //根据 settle_service_sn 判断记录是否存在
        SettleCarOwnerCheckDetailDO carOwnerCheckDetailDO = carOwnerCheckDetailDOMapper.selectByPackageOrderSn(checkDetail.getSettleServiceSn());
        if(carOwnerCheckDetailDO != null){
            //目前没有修改的需求
            return SettleDataOperateEnum.UPDATE.getCode();
        }

        carOwnerCheckDetailDOMapper.insertSelective(checkDetail);
        return SettleDataOperateEnum.INSERT.getCode();
    }

    @Override
    public void updateServicePackageRebate(SettleShopCheckDetailDO shopCheckDetailDO) {
        if(shopCheckDetailDO==null){
            return;
        }
        Integer cooperationModeId = shopCheckDetailDO.getCooperationModeId();
        if(CooperationModeEnum.GIVE_INSURANCE.getCode().equals(cooperationModeId)
                && SettleProjectEnum.BIZ_REBATE.getCode().equals(shopCheckDetailDO.getSettleProjectId())){
            List<String> insuranceOrderSnList = new ArrayList<>();
            insuranceOrderSnList.add(shopCheckDetailDO.getInsuranceOrderSn());
            SettleShopCheckDetailQueryPO queryPO = new SettleShopCheckDetailQueryPO();
            queryPO.setInsuranceOrderSnList(insuranceOrderSnList);
            queryPO.setSettleProjectId(SettleProjectEnum.PACKAGE_REBATE.getCode());
            List<SettleShopCheckDetailDO> detailDOList = shopCheckDetailDOMapper.selectByCondition(queryPO);
            if(detailDOList.isEmpty()){
                return;
            }
            SettleShopCheckDetailDO packageRebate = detailDOList.get(0); //服务包返利信息
            if(packageRebate.getErpFlag()==0){
                return;
            }
            SettleShopCheckDetailDO updatePackageRebate = new SettleShopCheckDetailDO();
            updatePackageRebate.setId(packageRebate.getId());
            updatePackageRebate.setErpFlag(0);
            shopCheckDetailDOMapper.updateByPrimaryKeySelective(updatePackageRebate);

            //同步操作记录给商业险记录
            shopCheckDetailDO.setAuditTime(packageRebate.getAuditTime());
            shopCheckDetailDO.setAuditPeopleName(packageRebate.getAuditPeopleName());
            shopCheckDetailDO.setSettleTime(packageRebate.getSettleTime());
            shopCheckDetailDO.setSettlePeopleName(packageRebate.getSettlePeopleName());
            shopCheckDetailDO.setSettleFeeStatus(packageRebate.getSettleFeeStatus());
            shopCheckDetailDO.setBalanceStatus(packageRebate.getBalanceStatus());
            shopCheckDetailDO.setApplyTime(packageRebate.getApplyTime());
            shopCheckDetailDO.setApplyPeopleName(packageRebate.getApplyPeopleName());
        }
    }

/*
    @Override
    public void settleShopBaseForPackage(SettleShopCheckDetailDO checkDetailDO, Integer packageStatus) {
        if(checkDetailDO==null){
            return;
        }
        try {
            Integer shopId = checkDetailDO.getAgentId();
            // 新增待生效服务报数量
            settleShopBaseBiz.addWaitEffectPackageNum(1, shopId);
            if(!ServicePackageStatusEnum.NOT_EFFECT.getCode().equals(packageStatus)){
                // 新增待发货的服务包数量
                settleShopBaseBiz.changePackageWaitEffectToWait(1, shopId);
            }

            // 新增未结算现金
            settleShopBaseBiz.addPayableCashAmount(checkDetailDO.getSettleFee(), shopId);
        }catch (Exception e){
            log.error("add settle shop base for package error, checkDetailDO="+ JsonUtil.objectToStr(checkDetailDO), e);
        }
    }

    @Override
    public void settleShopBaseForForm(List<SettleShopCheckDetailDO> checkDetailDOList) {
        if(CollectionUtils.isEmpty(checkDetailDOList)){
            return;
        }
        Integer settleRuleType = null;
        Integer shopId = null;
        BigDecimal settleFeeAmount = BigDecimal.ZERO;
        try {
            for(SettleShopCheckDetailDO detailDO : checkDetailDOList){
                settleRuleType = detailDO.getSettleRuleType();
                shopId = detailDO.getAgentId();
                settleFeeAmount = settleFeeAmount.add(detailDO.getSettleFee());
            }
            if(SettleRuleTypeEnum.REWARD.getCode().equals(settleRuleType)){
                //新增未结算奖励金
                settleShopBaseBiz.addPayableBonusAmount(settleFeeAmount, shopId);

            }else if(SettleRuleTypeEnum.CASH.getCode().equals(settleRuleType)){
                //新增未结算现金
                settleShopBaseBiz.addPayableCashAmount(settleFeeAmount, shopId);
            }

        }catch (Exception e){
//            StringBuilder msg = new StringBuilder();
//            msg.append("add settle shop base for form error");
//            msg.append(", settleRuleType=").append(settleRuleType);
//            msg.append(", shopId=").append(shopId);
//            msg.append(", settleFeeAmount=").append(settleFeeAmount);

            log.error("add settle shop base for form error, checkDetailDOList="
                    + JsonUtil.objectToStr(checkDetailDOList), e);
        }
    }
*/

    @Override
    public String getInsuranceCompanyName(Integer companyId){
        if(companyId==null){
            return "";
        }
        try {
            Result<String> result = insuranceBiz.getInsureCompanyNameById(companyId);
            if(result.isSuccess()){
                return result.getData();
            }
        }catch (Exception e){
            log.error("getInsuranceCompanyName error, companyId:"+companyId, e);
        }
        return "";
    }
    @Override
    public String getCooperationModeName(Integer cooperationModeId){
        if(cooperationModeId==null){
            return "";
        }
        try {
            return insuranceDicBiz.getCooperationModeNameByDicId(cooperationModeId);
        }catch (Exception e){
            log.error("getCooperationModeName error, cooperationModeId:"+cooperationModeId, e);
        }
        return "";
    }
    @Override
    public String getAccountMobile(Integer shopId){
        if(shopId==null){
            return "";
        }
        try {
            AccountDTO accountDTO = extShopInfoService.getAccountByShopId(shopId);
            if(accountDTO != null){
                return accountDTO.getMobile();
            }
        }catch (Exception e){
            log.error("getAccountByShopId error, shopId:"+shopId, e);
        }
        return "";
    }

    @Override
    public Integer getAgentTag(Integer shopId) {
        if(shopId==null || shopId<1){
            return null;
        }
        try {
            List<ShopTagDTO> tagDTOList = extShopInfoService.getTagsByShopId(shopId);
            if(CollectionUtils.isEmpty(tagDTOList)){
                return null;
            }
            for(ShopTagDTO tagDTO : tagDTOList){
                if(ConstantBean.YUN_XIU_TAG_KEYS.contains(tagDTO.getTagKey())){
                    return AgentTagEnum.YUN_XIU.getCode();
                }
            }
            return AgentTagEnum.DANG_KOU.getCode();
        }catch (Exception e){
            log.error("getTagsByShopId error, shopId:"+shopId, e);
        }
        return null;
    }

}
