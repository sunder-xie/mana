package com.tqmall.mana.biz.manager.settle.settleCalculate;

import com.tqmall.mana.beans.BO.settle.SettleRateBO;
import com.tqmall.mana.beans.entity.settle.*;
import com.tqmall.mana.beans.param.settle.ShopSettleRateQueryParam;
import com.tqmall.mana.biz.manager.settle.config.SettleInsuranceCompanyRuleBiz;
import com.tqmall.mana.biz.manager.settle.config.SettleShopRuleBiz;
import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettleBasicMsg;
import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettleFormMsg;
import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettleMaterialAllowanceMsg;
import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettlePackageMsg;
import com.tqmall.mana.component.annotation.lock.ManaLock;
import com.tqmall.mana.component.enums.SettleDataOperateEnum;
import com.tqmall.mana.component.enums.insurance.dict.*;
import com.tqmall.mana.component.redis.RedisKeyBean;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.mana.ManaUtil;
import com.tqmall.mana.dao.mapper.settle.SettleCarOwnerCheckDetailDOMapper;
import com.tqmall.mana.dao.mapper.settle.SettleShopCheckDetailExtendDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huangzhangting on 17/1/22.
 */
@Slf4j
@Service
public class InsuranceSettleCalculateBizImpl implements InsuranceSettleCalculateBiz {
    @Autowired
    private SettleCarOwnerCheckDetailDOMapper carOwnerCheckDetailDOMapper;
    @Autowired
    private SettleInsuranceCompanyRuleBiz insuranceCompanyRuleBiz;
    @Autowired
    private InsuranceSettleDataOperateBiz settleDataOperateBiz;
    @Autowired
    private SettleShopRuleBiz settleShopRuleBiz;
    @Autowired
    private SettleShopCheckDetailExtendDOMapper shopCheckDetailExtendDOMapper;


    /**
     * 必要属性校验，校验不通过的消息，直接丢弃
     */
    private boolean checkMsg(InsuranceSettleBasicMsg basicMsg){
        if(basicMsg==null){
            return false;
        }
        if(basicMsg.getCooperationModeId()==null){
            return false;
        }
        if(StringUtils.isEmpty(basicMsg.getInsuranceOrderSn())){
            return false;
        }
        if(basicMsg.getInsuranceCompanyId()==null){
            return false;
        }

        return true;
    }
    private void checkFormMsg(InsuranceSettleBasicMsg basicMsg){
        List<InsuranceSettleFormMsg> formMsgList = basicMsg.getFormMsgList();
        if(!CollectionUtils.isEmpty(formMsgList)){
            for(InsuranceSettleFormMsg formMsg : formMsgList){
                if(StringUtils.isEmpty(formMsg.getInsuredFormNo())){
                    basicMsg.setFormMsgList(null);
                    log.error("not calculate settle form data, basicMsg:{}", JsonUtil.objectToStr(basicMsg));
                    break;
                }
            }
        }
    }

    //先暂时不使用redis锁，用了之后，事务会不生效（有待测试）
    @ManaLock(lockKeyName = RedisKeyBean.LOCK_CALCULATE_SETTLE_DATA)
    @Override
    public void lockCalculateSettleData(InsuranceSettleBasicMsg basicMsg) {
//        if(!checkMsg(basicMsg)){
//            log.error("not calculate settle data, basicMsg:{}", JsonUtil.objectToStr(basicMsg));
//            return;
//        }
//        calculateSettleData(basicMsg);
    }

    @Transactional
    @Override
    public void calculateSettleData(InsuranceSettleBasicMsg basicMsg, boolean isMqInvoke) {
        if(!checkMsg(basicMsg)){
            log.error("not calculate settle data, basicMsg:{}", JsonUtil.objectToStr(basicMsg));
            return;
        }

        log.info("calculate settle data, param:{}", JsonUtil.objectToStr(basicMsg));

        //设置门店账号
        basicMsg.setAgentAccount(settleDataOperateBiz.getAccountMobile(basicMsg.getAgentId()));
        //设置门店标签
        basicMsg.setAgentTag(settleDataOperateBiz.getAgentTag(basicMsg.getAgentId()));

        //服务包结算数据
        calculateSettleDataForPackage(basicMsg, isMqInvoke);

        checkFormMsg(basicMsg);
        //保单结算数据
        calculateSettleDataForForm(basicMsg);

    }

    /** 计算服务包结算金额 */
    private void calculateSettleDataForPackage(InsuranceSettleBasicMsg basicMsg, boolean isMqInvoke){
        InsuranceSettlePackageMsg packageMsg = basicMsg.getPackageMsg();
        if(packageMsg==null){
            return;
        }

        if(isMqInvoke) {
            //如果是第二次支付
            BigDecimal secondPaidAmount = packageMsg.getSecondPaidAmount();
            if (secondPaidAmount != null && secondPaidAmount.compareTo(BigDecimal.ZERO) > 0) {
                packageSecondPay(packageMsg, basicMsg);
                return;
            }
        }

        Date now = new Date();
        //合作模式
        Integer cooperationModeId = basicMsg.getCooperationModeId();
        String cooperationModeName = settleDataOperateBiz.getCooperationModeName(cooperationModeId);

        SettleRateBO settleRateBO = getSettleRateBO(basicMsg);

        /** settle_service_check_detail 表数据 */
        SettleServiceCheckDetailDO serviceDO = convertServiceCheckDetail(basicMsg, settleRateBO);
        serviceDO.setGmtCreate(now);
        //如果服务包数据已经存在，就不再进行后续处理了
        String insuranceOrderSn = basicMsg.getInsuranceOrderSn();
        Integer operateCode = settleDataOperateBiz.insertOrUpdateServiceCheckDetail(insuranceOrderSn, serviceDO, basicMsg.getAgentId());

//        if(!SettleDataOperateEnum.INSERT.getCode().equals(operateCode)){
//            return;
//        }

        //保险公司
        Integer insuranceCompanyId = basicMsg.getInsuranceCompanyId();
        String insuranceCompanyName = settleDataOperateBiz.getInsuranceCompanyName(insuranceCompanyId);

        /** settle_shop_check_detail 表数据 */
        SettleShopCheckDetailDO shopDetailDO = convertShopCheckDetail(basicMsg, settleRateBO);
        shopDetailDO.setGmtCreate(now);
        shopDetailDO.setInsuranceCompanyId(insuranceCompanyId);
        shopDetailDO.setInsuranceCompanyName(insuranceCompanyName);
        shopDetailDO.setCooperationModeId(cooperationModeId);
        shopDetailDO.setCooperationModeName(cooperationModeName);

        /** settle_car_owner_check_detail 表数据 */
        //只有 买服务包送保险 模式，才有车主对账数据
        if(CooperationModeEnum.GIVE_INSURANCE.getCode().equals(cooperationModeId)) {
            SettleCarOwnerCheckDetailDO carOwnerDO = convertCarOwnerCheckDetail(basicMsg);
            carOwnerDO.setGmtCreate(now);
            carOwnerDO.setInsuranceCompanyId(insuranceCompanyId);
            carOwnerDO.setInsuranceCompanyName(insuranceCompanyName);
            carOwnerDO.setCooperationModeId(cooperationModeId);

            settleDataOperateBiz.insertOrUpdateCarOwnerCheckDetail(insuranceOrderSn, carOwnerDO);

            /** 服务包第一次支付，erp那边需要单独查询出服务包返利数据 */
            shopDetailDO.setErpFlag(1);
        }

        operateCode = settleDataOperateBiz.insertOrUpdateShopCheckDetail(insuranceOrderSn, shopDetailDO);
        if (SettleDataOperateEnum.INSERT.getCode().equals(operateCode)) {
            /** 门店结算基础信息表，统计数据 */
//            settleDataOperateBiz.settleShopBaseForPackage(shopDetailDO, serviceDO.getSettlePackageStatus());
        }
    }
    //根据物料订单编码，更新 settle_car_owner_check_detail 表数据
    private void packageSecondPay(InsuranceSettlePackageMsg packageMsg, InsuranceSettleBasicMsg basicMsg){
        String packageOrderSn = packageMsg.getPackageOrderSn();
        if(StringUtils.isEmpty(packageOrderSn)){
            log.info("packageSecondPay orderSn is empty, packageMsg:{}", JsonUtil.objectToStr(packageMsg));
            return;
        }
        SettleCarOwnerCheckDetailDO checkDetailDO = carOwnerCheckDetailDOMapper.selectByPackageOrderSn(packageOrderSn);
        if(checkDetailDO==null){
            log.info("packageSecondPay can not find data, packageMsg:{}", JsonUtil.objectToStr(packageMsg));
            return;
        }
        SettleCarOwnerCheckDetailDO carOwnerDO = new SettleCarOwnerCheckDetailDO();
        carOwnerDO.setId(checkDetailDO.getId());
        carOwnerDO.setSecondPayId(packageMsg.getSecondPayId());
        carOwnerDO.setSecondPayNo(packageMsg.getSecondPayNo());
        carOwnerDO.setSecondPaidAmount(packageMsg.getSecondPaidAmount());
        carOwnerDO.setGmtSecondPaid(packageMsg.getGmtSecondPaid());
        carOwnerDO.setSecondPayStatus(PayStatusEnum.PAY.getId());
        carOwnerDO.setGmtModified(new Date());

        //更新车主信息，因为二次支付时的车主信息，可能跟一次支付时的不一致，产品要求，以第二次的为准
        boolean carOwnerInfoDiffFlag = checkCarOwnerInfoDiff(checkDetailDO, carOwnerDO, basicMsg);

        carOwnerCheckDetailDOMapper.updateByPrimaryKeySelective(carOwnerDO);

        //门店对账表也需要更新
        if(carOwnerInfoDiffFlag){
            SettleShopCheckDetailDO shopCheckDetailDO = new SettleShopCheckDetailDO();
            shopCheckDetailDO.setGmtModified(carOwnerDO.getGmtModified());
//            shopCheckDetailDO.setVehicleSn(carOwnerDO.getVehicleSn());
            shopCheckDetailDO.setNewVehicleSn(carOwnerDO.getNewVehicleSn());
            shopCheckDetailDO.setCarOwnerName(carOwnerDO.getVehiclePeopleName());
            shopCheckDetailDO.setInsuranceOrderSn(basicMsg.getInsuranceOrderSn());
            shopCheckDetailDO.setSettleProjectId(SettleProjectEnum.PACKAGE_REBATE.getCode());
            settleDataOperateBiz.updateShopCheckDetail(shopCheckDetailDO);
        }
    }
    //只是为了兼容老的逻辑，因为之前车主姓名取错字段了，还没有调整
    private String getCarOwnerName(InsuranceSettleBasicMsg basicMsg){
        String carOwnerName = basicMsg.getCarOwnerName();
        if(carOwnerName==null){
            return basicMsg.getBeAppliedName();
        }
        return carOwnerName;
    }
    private boolean checkCarOwnerInfoDiff(SettleCarOwnerCheckDetailDO checkDetailDO, SettleCarOwnerCheckDetailDO carOwnerDO,
                                          InsuranceSettleBasicMsg basicMsg){
        boolean carOwnerInfoDiffFlag = false;
        String carOwnerName = getCarOwnerName(basicMsg);
        String vehicleSn = basicMsg.getVehicleSn();
        if(!checkDetailDO.getVehicleSn().equals(vehicleSn)) {
//            carOwnerDO.setVehicleSn(vehicleSn);
            carOwnerDO.setNewVehicleSn(vehicleSn);
            carOwnerInfoDiffFlag = true;
        }
        if(!checkDetailDO.getVehiclePeopleName().equals(carOwnerName)){
            carOwnerDO.setVehiclePeopleName(carOwnerName);
            carOwnerInfoDiffFlag = true;
        }

        return carOwnerInfoDiffFlag;
    }


    private SettleServiceCheckDetailDO convertServiceCheckDetail(InsuranceSettleBasicMsg basicMsg, SettleRateBO settleRateBO){
        InsuranceSettlePackageMsg packageMsg = basicMsg.getPackageMsg();

        SettleServiceCheckDetailDO serviceDO = new SettleServiceCheckDetailDO();
        serviceDO.setInsuranceOrderSn(basicMsg.getInsuranceOrderSn());
        serviceDO.setSettlePackageOrderSn(packageMsg.getPackageOrderSn());
        serviceDO.setSettlePackageName(packageMsg.getPackageName());
        serviceDO.setSettlePackagePrice(packageMsg.getPackagePrice());
        serviceDO.setGmtFirstPaid(packageMsg.getGmtFirstPaid());
        if(StringUtils.isEmpty(serviceDO.getSettlePackageOrderSn())){
            serviceDO.setSettlePackageStatus(ServicePackageStatusEnum.NOT_EFFECT.getCode());
        }
        //有配置服务包工时费，才存储工时费
        if(settleRateBO!=null){
            serviceDO.setSettlePackageFee(packageMsg.getPackageFee());
        }
        return serviceDO;
    }
    //手动同步数据时，会用到
    private void fillInInsuredFee(SettleCarOwnerCheckDetailDO carOwnerDO, List<InsuranceSettleFormMsg> formMsgList){
        if(CollectionUtils.isEmpty(formMsgList)){
            return;
        }
        for(InsuranceSettleFormMsg formMsg : formMsgList){
            if(InsuranceTypeEnum.BIZ_INSURANCE.getCode().equals(formMsg.getInsuranceTypeId())){
                carOwnerDO.setSyPayableInsuredFee(formMsg.getInsuredFee());
            }else{
                BigDecimal tax = formMsg.getTax();
                BigDecimal insuredFee = formMsg.getInsuredFee();
                if(tax!=null && insuredFee!=null){
                    insuredFee = insuredFee.subtract(tax);
                }
                carOwnerDO.setJqTaxFee(tax);
                carOwnerDO.setJqPayableInsuredFee(insuredFee);
            }
        }
    }
    private SettleCarOwnerCheckDetailDO convertCarOwnerCheckDetail(InsuranceSettleBasicMsg basicMsg){
        InsuranceSettlePackageMsg packageMsg = basicMsg.getPackageMsg();

        SettleCarOwnerCheckDetailDO carOwnerDO = new SettleCarOwnerCheckDetailDO();
        carOwnerDO.setInsuranceOrderSn(basicMsg.getInsuranceOrderSn());
        carOwnerDO.setVehiclePeopleName(getCarOwnerName(basicMsg));
        carOwnerDO.setVehicleSn(basicMsg.getVehicleSn());
        carOwnerDO.setAgentId(basicMsg.getAgentId());
        carOwnerDO.setAgentName(basicMsg.getAgentName());
        carOwnerDO.setAgentAccount(basicMsg.getAgentAccount());
        carOwnerDO.setAgentTag(basicMsg.getAgentTag());
        carOwnerDO.setSettleServiceSn(packageMsg.getPackageOrderSn());
        carOwnerDO.setFirstPayId(packageMsg.getFirstPayId());
        carOwnerDO.setFirstPayNo(packageMsg.getFirstPayNo());
        carOwnerDO.setFirstPaidAmount(packageMsg.getFirstPaidAmount());
        carOwnerDO.setGmtFirstPaid(packageMsg.getGmtFirstPaid());
        carOwnerDO.setFirstPayStatus(PayStatusEnum.PAY.getId());

        BigDecimal secondPaidAmount = packageMsg.getSecondPaidAmount();
        if(secondPaidAmount!=null && secondPaidAmount.compareTo(BigDecimal.ZERO)>0){
            carOwnerDO.setSecondPayId(packageMsg.getSecondPayId());
            carOwnerDO.setSecondPayNo(packageMsg.getSecondPayNo());
            carOwnerDO.setSecondPaidAmount(secondPaidAmount);
            carOwnerDO.setGmtSecondPaid(packageMsg.getGmtSecondPaid());
            carOwnerDO.setSecondPayStatus(PayStatusEnum.PAY.getId());
        }
        fillInInsuredFee(carOwnerDO, basicMsg.getFormMsgList());
        return carOwnerDO;
    }
    private SettleShopCheckDetailDO convertShopCheckDetail(InsuranceSettleBasicMsg basicMsg, SettleRateBO settleRateBO){
        InsuranceSettlePackageMsg packageMsg = basicMsg.getPackageMsg();

        SettleShopCheckDetailDO shopDetailDO = new SettleShopCheckDetailDO();
        shopDetailDO.setBizSn(packageMsg.getPackageOrderSn());
        shopDetailDO.setInsuranceOrderSn(basicMsg.getInsuranceOrderSn());
        shopDetailDO.setCarOwnerName(getCarOwnerName(basicMsg));
        shopDetailDO.setVehicleSn(basicMsg.getVehicleSn());
        shopDetailDO.setAgentId(basicMsg.getAgentId());
        shopDetailDO.setAgentName(basicMsg.getAgentName());
        shopDetailDO.setAgentAccount(basicMsg.getAgentAccount());
        shopDetailDO.setAgentTag(basicMsg.getAgentTag());
        shopDetailDO.setSettleRuleType(SettleRuleTypeEnum.CASH.getCode());
        shopDetailDO.setSettleProjectId(SettleProjectEnum.PACKAGE_REBATE.getCode());
        shopDetailDO.setSettleProjectName(SettleProjectEnum.PACKAGE_REBATE.getDesc());
        shopDetailDO.setSettleFee(packageMsg.getPackageFee());

        /** 从配置表获取比例 */
        setSettleCondition(basicMsg, shopDetailDO, settleRateBO);

        return shopDetailDO;
    }
    private SettleRateBO getSettleRateBO(InsuranceSettleBasicMsg basicMsg){
        ShopSettleRateQueryParam rateQueryParam = new ShopSettleRateQueryParam();
        rateQueryParam.setInsuranceCompanyId(basicMsg.getInsuranceCompanyId());
        rateQueryParam.setCooperationMode(basicMsg.getCooperationModeId());
        rateQueryParam.setShopId(basicMsg.getAgentId());
        rateQueryParam.setCityCode(basicMsg.getInsuredCityCode());
        rateQueryParam.setRebateStandard(RebateStandardEnum.PACKAGE.getCode());

        return settleShopRuleBiz.getSettleRate(rateQueryParam);
    }
    private void setSettleCondition(InsuranceSettleBasicMsg basicMsg, SettleShopCheckDetailDO shopDetailDO, SettleRateBO settleRateBO){
        if(settleRateBO==null){
            //未配置，则不设置结算金额
            shopDetailDO.setSettleFee(null);
            //买保险送服务包模式
            if(CooperationModeEnum.GIVE_PACKAGE.getCode().equals(basicMsg.getCooperationModeId())){
                shopDetailDO.setSettleConditionId(SettleConditionEnum.INSURED_DATE.getCode());
                shopDetailDO.setSettleConditionName(SettleConditionEnum.INSURED_DATE.getDesc());
                List<InsuranceSettleFormMsg> formMsgList = basicMsg.getFormMsgList();
                if(!CollectionUtils.isEmpty(formMsgList)){
                    for(InsuranceSettleFormMsg formMsg : formMsgList) {
                        if(InsuranceTypeEnum.BIZ_INSURANCE.getCode().equals(formMsg.getInsuranceTypeId())) {
                            shopDetailDO.setSettleConditionTime(formMsg.getInsuredStartTime());
                        }
                    }
                }
            }else {
                shopDetailDO.setSettleConditionId(SettleConditionEnum.PACKAGE_PAY_DATE.getCode());
                shopDetailDO.setSettleConditionName(SettleConditionEnum.PACKAGE_PAY_DATE.getDesc());
                shopDetailDO.setSettleConditionTime(basicMsg.getPackageMsg().getGmtFirstPaid());
            }
        }else{
            Integer settleCondition = settleRateBO.getSettleCondition();
            if(SettleConditionEnum.INSURED_DATE.getCode().equals(settleCondition)){
                shopDetailDO.setSettleConditionId(SettleConditionEnum.INSURED_DATE.getCode());
                shopDetailDO.setSettleConditionName(SettleConditionEnum.INSURED_DATE.getDesc());
                List<InsuranceSettleFormMsg> formMsgList = basicMsg.getFormMsgList();
                if(!CollectionUtils.isEmpty(formMsgList)){
                    for(InsuranceSettleFormMsg formMsg : formMsgList) {
                        if(InsuranceTypeEnum.BIZ_INSURANCE.getCode().equals(formMsg.getInsuranceTypeId())) {
                            shopDetailDO.setSettleConditionTime(formMsg.getInsuredStartTime());
                        }
                    }
                }
            }else if(SettleConditionEnum.SIGN_DATE.getCode().equals(settleCondition)){
                shopDetailDO.setSettleConditionId(SettleConditionEnum.SIGN_DATE.getCode());
                shopDetailDO.setSettleConditionName(SettleConditionEnum.SIGN_DATE.getDesc());
                List<InsuranceSettleFormMsg> formMsgList = basicMsg.getFormMsgList();
                if(!CollectionUtils.isEmpty(formMsgList)){
                    for(InsuranceSettleFormMsg formMsg : formMsgList) {
                        if(InsuranceTypeEnum.BIZ_INSURANCE.getCode().equals(formMsg.getInsuranceTypeId())) {
                            shopDetailDO.setSettleConditionTime(formMsg.getBillSignTime());
                        }
                    }
                }
            }else {
                shopDetailDO.setSettleConditionId(SettleConditionEnum.PACKAGE_PAY_DATE.getCode());
                shopDetailDO.setSettleConditionName(SettleConditionEnum.PACKAGE_PAY_DATE.getDesc());
                shopDetailDO.setSettleConditionTime(basicMsg.getPackageMsg().getGmtFirstPaid());
            }
        }
    }


    /** 计算保单结算金额 */
    private void calculateSettleDataForForm(InsuranceSettleBasicMsg basicMsg){
        List<InsuranceSettleFormMsg> formMsgList = basicMsg.getFormMsgList();
        if(CollectionUtils.isEmpty(formMsgList)){
            return;
        }

        Date now = new Date();
        Integer insuranceCompanyId = basicMsg.getInsuranceCompanyId();
        String insuranceCompanyName = settleDataOperateBiz.getInsuranceCompanyName(insuranceCompanyId);

        List<SettleShopCheckDetailDO> shopCheckDetailDOList = new ArrayList<>();
        boolean companySettleFlag = false;
        String insuranceOrderSn = basicMsg.getInsuranceOrderSn();
        for(InsuranceSettleFormMsg formMsg : formMsgList){
            if(formMsg.getInsuredFormNo()==null){
                log.error("保单号为空, formMsg:{}", JsonUtil.objectToStr(formMsg));
                continue;
            }
            /** settle_form 表数据 */
            SettleFormDO settleFormDO = convertSettleForm(basicMsg, formMsg);
            settleFormDO.setGmtCreate(now);
            //如果保单数据已存在，则不再进行后续处理
            Integer operateCode = settleDataOperateBiz.insertOrUpdateSettleForm(insuranceOrderSn, settleFormDO);
            if(!SettleDataOperateEnum.INSERT.getCode().equals(operateCode)){
                continue;
            }
            companySettleFlag = true;

            /** settle_shop_check_detail 表数据 */
            SettleShopCheckDetailDO shopCheckDetailDO = convertShopCheckDetailForForm(basicMsg, formMsg);
            shopCheckDetailDO.setGmtCreate(now);
            shopCheckDetailDO.setInsuranceCompanyId(insuranceCompanyId);
            shopCheckDetailDO.setInsuranceCompanyName(insuranceCompanyName);
            shopCheckDetailDO.setErpFlag(1);

            /**
             * 如果是买服务包送保险模式，更新关联的服务包返利记录erp_flag=0，
             * 并且将商业险的操作信息同步，跟服务包返利记录的操作信息一致
             * */
            settleDataOperateBiz.updateServicePackageRebate(shopCheckDetailDO);

            operateCode = settleDataOperateBiz.insertOrUpdateShopCheckDetail(insuranceOrderSn, shopCheckDetailDO);
            if(SettleDataOperateEnum.INSERT.getCode().equals(operateCode)) {
                /** 用于门店结算基础信息表统计数据 */
                shopCheckDetailDOList.add(shopCheckDetailDO);
            }
        }

        /** 计算保险公司结算数据 */
        if(companySettleFlag){
            try {
                insuranceCompanyRuleBiz.calculateInsuredRoyaltyFee(basicMsg);
            }catch (Exception e){
                log.error("calculateInsuredRoyaltyFee error, basicMsg:"+JsonUtil.objectToStr(basicMsg), e);
            }
        }

        /** 门店结算基础信息表，统计数据 */
//        settleDataOperateBiz.settleShopBaseForForm(shopCheckDetailDOList);

    }
    private SettleFormDO convertSettleForm(InsuranceSettleBasicMsg basicMsg, InsuranceSettleFormMsg formMsg){
        SettleFormDO settleFormDO = new SettleFormDO();
        settleFormDO.setInsuranceFormId(formMsg.getId());
        settleFormDO.setInsuredFormNo(formMsg.getInsuredFormNo());
        settleFormDO.setCooperationModeId(formMsg.getCooperationModeId());
        settleFormDO.setBillSignTime(formMsg.getBillSignTime());
        settleFormDO.setInsuredApplyNo(formMsg.getInsuredApplyNo());
        settleFormDO.setInsuredStartTime(formMsg.getInsuredStartTime());

        settleFormDO.setInsuranceOrderSn(basicMsg.getInsuranceOrderSn());
        settleFormDO.setApplicantName(basicMsg.getApplicantName());
        settleFormDO.setBeAppliedName(basicMsg.getBeAppliedName());
        settleFormDO.setVehicleSn(basicMsg.getVehicleSn());
        settleFormDO.setAgentId(basicMsg.getAgentId());
        settleFormDO.setAgentName(basicMsg.getAgentName());
        settleFormDO.setAgentAccount(basicMsg.getAgentAccount());
        settleFormDO.setAgentTag(basicMsg.getAgentTag());
        settleFormDO.setInsuredProvince(basicMsg.getInsuredProvince());
        settleFormDO.setInsuredProvinceCode(basicMsg.getInsuredProvinceCode());
        settleFormDO.setInsuredCity(basicMsg.getInsuredCity());
        settleFormDO.setInsuredCityCode(basicMsg.getInsuredCityCode());
        settleFormDO.setInsuranceCompanyId(basicMsg.getInsuranceCompanyId());

        settleFormDO.setInsuredFee(formMsg.getInsuredFee());
        Integer insuranceTypeId = formMsg.getInsuranceTypeId();
        settleFormDO.setInsuranceTypeId(insuranceTypeId);

        //交强险，保费需要减掉车船税，因为insurance那边的insured_fee是包含车船税的
        if(InsuranceTypeEnum.FORCE_INSURANCE.getCode().equals(insuranceTypeId)){
            BigDecimal tax = formMsg.getTax();
            if(tax!=null && tax.compareTo(BigDecimal.ZERO)>0){
                settleFormDO.setTaxFee(tax);
                BigDecimal insuredFee = formMsg.getInsuredFee().subtract(tax);
                settleFormDO.setInsuredFee(insuredFee);
                formMsg.setInsuredFee(insuredFee); //后续计算返利需要用到
            }

        }

        return settleFormDO;
    }

    private SettleShopCheckDetailDO convertShopCheckDetailForForm(InsuranceSettleBasicMsg basicMsg, InsuranceSettleFormMsg formMsg){
        Integer cooperationModeId = formMsg.getCooperationModeId();
        String cooperationModeName = settleDataOperateBiz.getCooperationModeName(cooperationModeId);

        SettleShopCheckDetailDO shopDetailDO = new SettleShopCheckDetailDO();
        shopDetailDO.setBizSn(formMsg.getInsuredFormNo());
        shopDetailDO.setInsuranceOrderSn(basicMsg.getInsuranceOrderSn());
        shopDetailDO.setCarOwnerName(getCarOwnerName(basicMsg));
        shopDetailDO.setVehicleSn(basicMsg.getVehicleSn());
        shopDetailDO.setAgentId(basicMsg.getAgentId());
        shopDetailDO.setAgentName(basicMsg.getAgentName());
        shopDetailDO.setAgentAccount(basicMsg.getAgentAccount());
        shopDetailDO.setAgentTag(basicMsg.getAgentTag());

        if(CooperationModeEnum.GIVE_REWARD.getCode().equals(cooperationModeId)){ //奖励金模式
//            shopDetailDO.setSettleRuleType(SettleRuleTypeEnum.REWARD.getCode());
            shopDetailDO.setSettleRuleType(SettleRuleTypeEnum.CASH.getCode());
            shopDetailDO.setSettleFeeStatus(SettleStatusEnum.NOT_PAY.getCode()); //奖励金直接设置为“未支付”
        }else {
            shopDetailDO.setSettleRuleType(SettleRuleTypeEnum.CASH.getCode());
        }

        shopDetailDO.setCooperationModeId(cooperationModeId);
        shopDetailDO.setCooperationModeName(cooperationModeName);

        //保险类别
        Integer insuranceTypeId = formMsg.getInsuranceTypeId();
        if(InsuranceTypeEnum.BIZ_INSURANCE.getCode().equals(insuranceTypeId)){
            shopDetailDO.setSettleProjectId(SettleProjectEnum.BIZ_REBATE.getCode());
            shopDetailDO.setSettleProjectName(SettleProjectEnum.BIZ_REBATE.getDesc());
        }else{
            shopDetailDO.setSettleProjectId(SettleProjectEnum.FORCE_REBATE.getCode());
            shopDetailDO.setSettleProjectName(SettleProjectEnum.FORCE_REBATE.getDesc());
        }

        //计算结算金额
        calculateSettleFee(shopDetailDO, basicMsg, formMsg);

        return shopDetailDO;
    }
    private void calculateSettleFee(SettleShopCheckDetailDO shopDetailDO,
                                    InsuranceSettleBasicMsg basicMsg, InsuranceSettleFormMsg formMsg){
        //结算比例
        BigDecimal settleRate = getSettleRate(shopDetailDO, basicMsg, formMsg);
        if(settleRate==null){
            settleRate = BigDecimal.ZERO;
        }
        //结算金额
        BigDecimal settleFee = formMsg.getInsuredFee().multiply(settleRate);
        //保留两位小数、四舍五入
        shopDetailDO.setSettleFee(ManaUtil.getDecimalHalfUp(settleFee));
        shopDetailDO.setSettleRate(settleRate);
    }

    //todo 获取配置的比例，暂时先写死
    private BigDecimal getSettleRateLocal(Integer cooperationModeId, Integer insuranceTypeId){
        BigDecimal settleRate = BigDecimal.ZERO;
        if(CooperationModeEnum.GIVE_REWARD.getCode().equals(cooperationModeId)){ //买保险送奖励金
            if(InsuranceTypeEnum.BIZ_INSURANCE.getCode().equals(insuranceTypeId)){ //商业险
                settleRate = new BigDecimal(0.37);
            }else{
                settleRate = new BigDecimal(0.08);
            }
        }else if(CooperationModeEnum.GIVE_PACKAGE.getCode().equals(cooperationModeId)){ //买保险送服务包
            if(InsuranceTypeEnum.BIZ_INSURANCE.getCode().equals(insuranceTypeId)){
                //settleRate = new BigDecimal(0.2);  //目前服务包模式，商业险不反比例；改成配置后，直接依赖于配置，程序不管配置对错
            }else{
                settleRate = new BigDecimal(0.08);
            }
        }else if(CooperationModeEnum.GIVE_INSURANCE.getCode().equals(cooperationModeId)){ //买服务包送保险
            if(InsuranceTypeEnum.BIZ_INSURANCE.getCode().equals(insuranceTypeId)){
                //settleRate = new BigDecimal(0.4);  //目前服务包模式，商业险不反比例
            }else{
                settleRate = new BigDecimal(0.08);
            }
        }
        return settleRate;
    }
    private BigDecimal getSettleRate(SettleShopCheckDetailDO shopDetailDO,
                                     InsuranceSettleBasicMsg basicMsg, InsuranceSettleFormMsg formMsg){
        Integer insuranceCompanyId = basicMsg.getInsuranceCompanyId();
        //保险模式
        Integer cooperationModeId = formMsg.getCooperationModeId();
        //保险类别
        Integer insuranceTypeId = formMsg.getInsuranceTypeId();
        //结算比例
        BigDecimal settleRate = null;

/*
        settleRate = getSettleRateLocal(cooperationModeId, insuranceTypeId);
        shopDetailDO.setSettleConditionId(SettleConditionEnum.INSURED_DATE.getCode());
        shopDetailDO.setSettleConditionName(SettleConditionEnum.INSURED_DATE.getDesc());
        shopDetailDO.setSettleConditionTime(formMsg.getInsuredStartTime());
*/

        /** 从配置表获取比例 */
        ShopSettleRateQueryParam rateQueryParam = new ShopSettleRateQueryParam();
        rateQueryParam.setInsuranceCompanyId(insuranceCompanyId);
        rateQueryParam.setCooperationMode(cooperationModeId);
        rateQueryParam.setShopId(basicMsg.getAgentId());
        rateQueryParam.setCityCode(basicMsg.getInsuredCityCode());
        rateQueryParam.setInsuredFee(formMsg.getInsuredFee());
        if(InsuranceTypeEnum.BIZ_INSURANCE.getCode().equals(insuranceTypeId)){
            rateQueryParam.setRebateStandard(RebateStandardEnum.BIZ_INSURANCE.getCode());
        }else if(InsuranceTypeEnum.FORCE_INSURANCE.getCode().equals(insuranceTypeId)){
            rateQueryParam.setRebateStandard(RebateStandardEnum.FORCE_INSURANCE.getCode());
        }

        SettleRateBO settleRateBO = settleShopRuleBiz.getSettleRate(rateQueryParam);
        log.info("保单结算，查询结算配置，参数：{}，结果：{}", JsonUtil.objectToStr(rateQueryParam), JsonUtil.objectToStr(settleRateBO));
        if(settleRateBO != null){
            settleRate = settleRateBO.getSettleRate();
            Integer settleCondition = settleRateBO.getSettleCondition();
            if(SettleConditionEnum.INSURED_DATE.getCode().equals(settleCondition)){
                shopDetailDO.setSettleConditionId(SettleConditionEnum.INSURED_DATE.getCode());
                shopDetailDO.setSettleConditionName(SettleConditionEnum.INSURED_DATE.getDesc());
                shopDetailDO.setSettleConditionTime(formMsg.getInsuredStartTime());

            }else if(SettleConditionEnum.SIGN_DATE.getCode().equals(settleCondition)){
                shopDetailDO.setSettleConditionId(SettleConditionEnum.SIGN_DATE.getCode());
                shopDetailDO.setSettleConditionName(SettleConditionEnum.SIGN_DATE.getDesc());
                shopDetailDO.setSettleConditionTime(formMsg.getBillSignTime());

            }
        }else{
            /*
            settleRate = getSettleRateLocal(cooperationModeId, insuranceTypeId);
            shopDetailDO.setSettleConditionId(SettleConditionEnum.INSURED_DATE.getCode());
            shopDetailDO.setSettleConditionName(SettleConditionEnum.INSURED_DATE.getDesc());
            shopDetailDO.setSettleConditionTime(formMsg.getInsuredStartTime());
            */
        }

        return settleRate;
    }


    /** 物料补贴结算数据 */
    //因为不涉及到多表操作的事务控制，所以直接使用分布式锁即可
    @ManaLock(lockKeyName = RedisKeyBean.LOCK_CALCULATE_MATERIAL_ALLOWANCE)
    @Override
    public void calculateSettleDataForMaterialAllowance(InsuranceSettleMaterialAllowanceMsg materialAllowanceMsg) {
        log.info("calculate material allowance, param:{}", JsonUtil.objectToStr(materialAllowanceMsg));

        if(materialAllowanceMsg == null) {
            return;
        }
        //应补贴金额
        BigDecimal payableAmount = materialAllowanceMsg.getPayableAmount();
        if(payableAmount==null || payableAmount.compareTo(BigDecimal.ZERO)<=0){
            log.info("calculateSettleDataForMaterialAllowance failed, payableAmount is error, insuranceOrderSn:{}, materialAllowanceMsg:{}",
                    materialAllowanceMsg.getInsuranceOrderSn(), JsonUtil.objectToStr(materialAllowanceMsg));
            return;
        }

        String insuranceOrderSn = materialAllowanceMsg.getInsuranceOrderSn();
        SettleShopCheckDetailDO shopDetailDO = new SettleShopCheckDetailDO();
        shopDetailDO.setBizSn(materialAllowanceMsg.getMaterialOrderSn());
        shopDetailDO.setInsuranceOrderSn(insuranceOrderSn);
        shopDetailDO.setCarOwnerName(materialAllowanceMsg.getCarOwnerName());
        //这么处理只是为了兼容老的逻辑，等bug修复后，删掉这段代码即可
        if(shopDetailDO.getCarOwnerName()==null){
            shopDetailDO.setCarOwnerName(materialAllowanceMsg.getBeAppliedName());
        }
        shopDetailDO.setVehicleSn(materialAllowanceMsg.getVehicleSn());
        shopDetailDO.setAgentId(materialAllowanceMsg.getAgentId());
        shopDetailDO.setAgentName(materialAllowanceMsg.getAgentName());
        shopDetailDO.setAgentAccount(settleDataOperateBiz.getAccountMobile(materialAllowanceMsg.getAgentId()));
        shopDetailDO.setAgentTag(settleDataOperateBiz.getAgentTag(materialAllowanceMsg.getAgentId()));
        shopDetailDO.setSettleRuleType(SettleRuleTypeEnum.CASH.getCode());
        shopDetailDO.setSettleProjectId(SettleProjectEnum.OIL_FILTER_REBATE.getCode());
        shopDetailDO.setSettleProjectName(SettleProjectEnum.OIL_FILTER_REBATE.getDesc());
        shopDetailDO.setSettleConditionId(SettleConditionEnum.ALLOWANCE_EFFECT.getCode());
        shopDetailDO.setSettleConditionName(SettleConditionEnum.ALLOWANCE_EFFECT.getDesc());
        shopDetailDO.setSettleConditionTime(materialAllowanceMsg.getAllowanceEffectTime());
        shopDetailDO.setSettleFeeStatus(SettleStatusEnum.NOT_PAY.getCode());

        shopDetailDO.setSettleFee(payableAmount);
        shopDetailDO.setGmtCreate(new Date());

        Integer insuranceCompanyId = materialAllowanceMsg.getInsuranceCompanyId();
        shopDetailDO.setInsuranceCompanyId(insuranceCompanyId);
        shopDetailDO.setInsuranceCompanyName(settleDataOperateBiz.getInsuranceCompanyName(insuranceCompanyId));

        Integer cooperationModeId = materialAllowanceMsg.getCooperationModeId();
        shopDetailDO.setCooperationModeId(cooperationModeId);
        shopDetailDO.setCooperationModeName(settleDataOperateBiz.getCooperationModeName(cooperationModeId));

        Integer flag = settleDataOperateBiz.insertOrUpdateShopCheckDetail(insuranceOrderSn, shopDetailDO);
        //往扩展表插入数据
        if(SettleDataOperateEnum.INSERT.getCode().equals(flag)){
            SettleShopCheckDetailExtendDO extendDO = new SettleShopCheckDetailExtendDO();
            extendDO.setGmtCreate(new Date());
            extendDO.setShopCheckDetailId(shopDetailDO.getId());
            extendDO.setAllowanceStatus(AllowanceStatusEnum.WSQ.getCode());
            extendDO.setWarehouseId(materialAllowanceMsg.getWhsId());
            extendDO.setWarehouseName(materialAllowanceMsg.getWhsName());
            extendDO.setMaterialType(materialAllowanceMsg.getMaterialType());
            extendDO.setMaterialNum(materialAllowanceMsg.getMaterialNum());

            shopCheckDetailExtendDOMapper.insertSelective(extendDO);
        }
    }

}
