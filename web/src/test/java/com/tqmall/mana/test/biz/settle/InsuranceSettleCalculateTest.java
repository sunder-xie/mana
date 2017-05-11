package com.tqmall.mana.test.biz.settle;

import com.tqmall.mana.beans.BO.settle.calculate.InsuranceSettleBasicBO;
import com.tqmall.mana.beans.BO.settle.calculate.InsuranceSettleFormBO;
import com.tqmall.mana.biz.manager.settle.settleCalculate.InsuranceSettleCalculateBiz;
import com.tqmall.mana.biz.manager.settle.settleCalculate.InsuranceSettleCalculateExtendBiz;
import com.tqmall.mana.biz.mq.insurance.settle.*;
import com.tqmall.mana.component.enums.insurance.dict.InsuranceTypeEnum;
import com.tqmall.mana.component.enums.insurance.dict.CooperationModeEnum;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by huangzhangting on 17/1/23.
 */
public class InsuranceSettleCalculateTest extends BaseTest {
    @Autowired
    private InsuranceSettleCalculateBiz insuranceSettleCalculateBiz;
    @Autowired
    private InsuranceSettleCalculateExtendBiz insuranceSettleCalculateExtendBiz;


    InsuranceSettleBasicMsg createBasicMsg(String insuranceOrderSn, Integer cooperationModeId){
        InsuranceSettleBasicMsg basicMsg = new InsuranceSettleBasicMsg();
        basicMsg.setCooperationModeId(cooperationModeId);
        basicMsg.setInsuranceOrderSn(insuranceOrderSn);
        basicMsg.setApplicantName("黄先生");
        basicMsg.setBeAppliedName("黄先生3");
        basicMsg.setVehicleSn("浙BdaSK");
        basicMsg.setAgentId(44841);
        basicMsg.setAgentName("管子测试郑州");
        basicMsg.setInsuranceCompanyId(1);
        basicMsg.setInsuredProvince("广东省");
        basicMsg.setInsuredProvinceCode("440000");
        basicMsg.setInsuredCity("广州市");
        basicMsg.setInsuredCityCode("440100");

        return basicMsg;
    }
    InsuranceSettlePackageMsg createPackageMsg(String packageOrderSn, boolean isSecondPay){
        InsuranceSettlePackageMsg packageMsg = new InsuranceSettlePackageMsg();
        packageMsg.setPackageOrderSn(packageOrderSn);
        packageMsg.setPackageName("AC0001");
        packageMsg.setPackagePrice(new BigDecimal(3000));
        packageMsg.setPackageFee(new BigDecimal(500));
        packageMsg.setFirstPayId(1);
        packageMsg.setFirstPayNo("firstPayNo11");
        packageMsg.setGmtFirstPaid(new Date());
        packageMsg.setFirstPaidAmount(new BigDecimal(1800));

        if (isSecondPay) {
            packageMsg.setSecondPayId(1);
            packageMsg.setSecondPayNo("secondPayNo22");
            packageMsg.setGmtSecondPaid(new Date());
            packageMsg.setSecondPaidAmount(new BigDecimal(500));
        }

        return packageMsg;
    }
    List<InsuranceSettleFormMsg> createFormMsgList(String insuranceOrderSn, Integer cooperationModeId){
        List<InsuranceSettleFormMsg> formMsgList = new ArrayList<>();
        InsuranceSettleFormMsg formMsg = createFormMsg(insuranceOrderSn, cooperationModeId, InsuranceTypeEnum.BIZ_INSURANCE.getCode());
        formMsg.setItemMsgList(createBizItemMsgList(formMsg.getId()));
        formMsgList.add(formMsg);

        formMsg = createFormMsg(insuranceOrderSn, cooperationModeId, InsuranceTypeEnum.FORCE_INSURANCE.getCode());
        formMsg.setItemMsgList(createForceItemMsgList(formMsg.getId()));
        formMsgList.add(formMsg);

        return formMsgList;
    }
    InsuranceSettleFormMsg createFormMsg(String insuranceOrderSn, Integer cooperationModeId, Integer insuranceTypeId){
        String formNo = insuranceOrderSn+"_"+cooperationModeId+"_"+insuranceTypeId;
        InsuranceSettleFormMsg formMsg = new InsuranceSettleFormMsg();
        formMsg.setId(1);
        formMsg.setInsuredApplyNo(formNo);
        formMsg.setInsuredFormNo(formNo);
        formMsg.setInsuranceTypeId(insuranceTypeId);
        formMsg.setCooperationModeId(cooperationModeId);
        formMsg.setBillSignTime(new Date());
        formMsg.setInsuredStartTime(new Date());
        formMsg.setInsuredFee(new BigDecimal(5000));

        return formMsg;
    }
    List<InsuranceSettleItemMsg> createBizItemMsgList(Integer insuranceFormId){
        List<InsuranceSettleItemMsg> list = new ArrayList<>();
        InsuranceSettleItemMsg itemMsg = new InsuranceSettleItemMsg();
        itemMsg.setInsuranceFormId(insuranceFormId);
        itemMsg.setInsuranceType(InsuranceTypeEnum.BIZ_INSURANCE.getCode());
        itemMsg.setInsuranceCategoryCode("033002");
        itemMsg.setInsuranceName("第三者责任险");
        itemMsg.setInsuranceFee(new BigDecimal(1000));
        list.add(itemMsg);
        return list;
    }
    List<InsuranceSettleItemMsg> createForceItemMsgList(Integer insuranceFormId){
        List<InsuranceSettleItemMsg> list = new ArrayList<>();
        InsuranceSettleItemMsg itemMsg = new InsuranceSettleItemMsg();
        itemMsg.setInsuranceFormId(insuranceFormId);
        itemMsg.setInsuranceType(InsuranceTypeEnum.FORCE_INSURANCE.getCode());
        itemMsg.setInsuranceCategoryCode("033005");
        itemMsg.setInsuranceName("全车盗抢险");
        itemMsg.setInsuranceFee(new BigDecimal(1000));
        list.add(itemMsg);
        return list;
    }

    @Test
    public void testCalculate_服务包第一次支付(){
        Integer cooperationModeId = CooperationModeEnum.GIVE_INSURANCE.getCode();
        InsuranceSettleBasicMsg basicMsg = createBasicMsg("hzt_test_insuranceOrderSn_0003", cooperationModeId);
        InsuranceSettlePackageMsg packageMsg = createPackageMsg("hzt_test_packageOrderSn_0003", false);
        basicMsg.setPackageMsg(packageMsg);
        insuranceSettleCalculateBiz.calculateSettleData(basicMsg, true);
    }

    @Test
    public void testCalculate_服务包第二次支付(){
        Integer cooperationModeId = CooperationModeEnum.GIVE_INSURANCE.getCode();
        InsuranceSettleBasicMsg basicMsg = createBasicMsg("hzt_test_insuranceOrderSn_0003", cooperationModeId);
        InsuranceSettlePackageMsg packageMsg = createPackageMsg("hzt_test_packageOrderSn_0003", true);
        basicMsg.setPackageMsg(packageMsg);
        insuranceSettleCalculateBiz.calculateSettleData(basicMsg, true);
    }

    @Test
    public void testCalculate_买服务包送保险_正式投保(){
        Integer cooperationModeId = CooperationModeEnum.GIVE_INSURANCE.getCode();
        String insuranceOrderSn = "hzt_test_insuranceOrderSn_0008";
        InsuranceSettleBasicMsg basicMsg = createBasicMsg(insuranceOrderSn, cooperationModeId);
        List<InsuranceSettleFormMsg> formMsgList = createFormMsgList(insuranceOrderSn, cooperationModeId);
        basicMsg.setFormMsgList(formMsgList);

        insuranceSettleCalculateBiz.calculateSettleData(basicMsg, true);
    }

    @Test
    public void testCalculate_买保险送奖励金(){
        Integer cooperationModeId = CooperationModeEnum.GIVE_REWARD.getCode();
        String insuranceOrderSn = "hzt_test_insuranceOrderSn_0010";
        InsuranceSettleBasicMsg basicMsg = createBasicMsg(insuranceOrderSn, cooperationModeId);
        List<InsuranceSettleFormMsg> formMsgList = createFormMsgList(insuranceOrderSn, cooperationModeId);
        basicMsg.setFormMsgList(formMsgList);
        insuranceSettleCalculateBiz.calculateSettleData(basicMsg, true);
    }

    @Test
    public void testCalculate_买保险送服务包(){
        Integer cooperationModeId = CooperationModeEnum.GIVE_PACKAGE.getCode();
        String code = "0007";
        String insuranceOrderSn = "hzt_test_insuranceOrderSn_"+code;
        InsuranceSettleBasicMsg basicMsg = createBasicMsg(insuranceOrderSn, cooperationModeId);
        List<InsuranceSettleFormMsg> formMsgList = createFormMsgList(insuranceOrderSn, cooperationModeId);
        basicMsg.setFormMsgList(formMsgList);
        InsuranceSettlePackageMsg packageMsg = createPackageMsg("hzt_test_packageOrderSn_"+code, false);
        basicMsg.setPackageMsg(packageMsg);
        insuranceSettleCalculateBiz.lockCalculateSettleData(basicMsg);
    }


    @Test
    public void testCalculate_机滤补贴(){
        //转json串有问题，待跟进
        String msg = "{\\\"agentId\\\":669749,\\\"agentName\\\":\\\"自动化云南新客户s\\\",\\\"allowanceEffectTime\\\":1488164090000,\\\"beAppliedName\\\":\\\"nina\\\",\\\"cooperationModeId\\\":2,\\\"insuranceCompanyId\\\":1,\\\"insuranceOrderSn\\\":\\\"EC1603000320170227095501R3b2144\\\",\\\"materialNum\\\":2,\\\"materialOrderSn\\\":\\\"B17022772871099\\\",\\\"materialType\\\":1,\\\"payableAmount\\\":20.00,\\\"vehicleSn\\\":\\\"粤0227202\\\",\\\"whsId\\\":4,\\\"whsName\\\":\\\"杭州仓\\\"}";
        InsuranceSettleMaterialAllowanceMsg allowanceMsg = JsonUtil.strToObject(msg, InsuranceSettleMaterialAllowanceMsg.class);

        allowanceMsg = new InsuranceSettleMaterialAllowanceMsg();
        allowanceMsg.setCooperationModeId(CooperationModeEnum.GIVE_PACKAGE.getCode());
        allowanceMsg.setInsuranceOrderSn("hzt_test_allowance_order_sn_001");
        allowanceMsg.setMaterialOrderSn("hzt_test_allowance_sn_001");
        allowanceMsg.setBeAppliedName("hzzd");
        allowanceMsg.setVehicleSn("浙B841BD");
        allowanceMsg.setAgentId(669749);
        allowanceMsg.setAgentName("自动化云南新客户s");
        allowanceMsg.setInsuranceCompanyId(1);
        allowanceMsg.setMaterialType(1);
        allowanceMsg.setPayableAmount(new BigDecimal(30));
        allowanceMsg.setAllowanceEffectTime(new Date());
        allowanceMsg.setWhsId(123);
        allowanceMsg.setWhsName("ceshi仓库");
        allowanceMsg.setMaterialNum(3);

        insuranceSettleCalculateBiz.calculateSettleDataForMaterialAllowance(allowanceMsg);

    }

    @Test
    public void testCalculate_1(){
        String msg = "{\\\"agentId\\\":669749,\\\"agentName\\\":\\\"自动化电销云修\\\",\\\"applicantName\\\":\\\"许一韵\\\",\\\"beAppliedName\\\":\\\"许一韵\\\",\\\"cooperationModeId\\\":2,\\\"formMsgList\\\":[{\\\"billSignTime\\\":1488955442000,\\\"cooperationModeId\\\":2,\\\"id\\\":26073,\\\"insuranceTypeId\\\":1,\\\"insuredApplyNo\\\":\\\"00200000603000120170001583\\\",\\\"insuredFee\\\":1300.00,\\\"insuredFormNo\\\":\\\"10200000603000120170000228\\\",\\\"insuredStartTime\\\":1488988800000,\\\"itemMsgList\\\":[{\\\"insuranceCategoryCode\\\":\\\"030000\\\",\\\"insuranceFee\\\":950.00,\\\"insuranceFormId\\\":26073,\\\"insuranceName\\\":\\\"交通事故责任强制保险\\\",\\\"insuranceType\\\":1}],\\\"tax\\\":350.00},{\\\"billSignTime\\\":1488955442000,\\\"cooperationModeId\\\":2,\\\"id\\\":26074,\\\"insuranceTypeId\\\":2,\\\"insuredApplyNo\\\":\\\"00200000603301120170082419\\\",\\\"insuredFee\\\":6183.74,\\\"insuredFormNo\\\":\\\"10200000603301120170008352\\\",\\\"insuredStartTime\\\":1488988800000,\\\"itemMsgList\\\":[{\\\"insuranceCategoryCode\\\":\\\"033001\\\",\\\"insuranceFee\\\":3908.46,\\\"insuranceFormId\\\":26074,\\\"insuranceName\\\":\\\"车损险\\\",\\\"insuranceType\\\":2},{\\\"insuranceCategoryCode\\\":\\\"033002\\\",\\\"insuranceFee\\\":1468.70,\\\"insuranceFormId\\\":26074,\\\"insuranceName\\\":\\\"第三者责任险\\\",\\\"insuranceType\\\":2}],\\\"tax\\\":0.00}],\\\"insuranceCompanyId\\\":1,\\\"insuranceOrderSn\\\":\\\"EC1603000320170308143551S172176\\\",\\\"insuredCity\\\":\\\"阳江市\\\",\\\"insuredCityCode\\\":\\\"441700\\\",\\\"insuredProvince\\\":\\\"广东省\\\",\\\"insuredProvinceCode\\\":\\\"440000\\\",\\\"packageMsg\\\":{\\\"packageFee\\\":1200.00,\\\"packageName\\\":\\\"C5000\\\",\\\"packagePrice\\\":0.02},\\\"vehicleSn\\\":\\\"\\\"}";

        msg = "{\"cooperationModeId\":2,\"insuranceOrderSn\":\"EC1603000320170308143551S172176\",\"applicantName\":\"许一韵\",\"beAppliedName\":\"许一韵\",\"vehicleSn\":\"\",\"agentId\":669749,\"agentName\":\"自动化电销云修\",\"insuranceCompanyId\":1,\"insuredProvinceCode\":\"440000\",\"insuredCityCode\":\"441700\",\"insuredProvince\":\"广东省\",\"insuredCity\":\"阳江市\",\"formMsgList\":[{\"id\":26073,\"insuredApplyNo\":\"00200000603000120170001583\",\"insuredFormNo\":\"10200000603000120170000228\",\"insuranceTypeId\":1,\"cooperationModeId\":2,\"billSignTime\":1488955442000,\"insuredStartTime\":1488988800000,\"insuredFee\":1300.00,\"tax\":350.00,\"itemMsgList\":[{\"insuranceFormId\":26073,\"insuranceType\":1,\"insuranceCategoryCode\":\"030000\",\"insuranceName\":\"交通事故责任强制保险\",\"insuranceFee\":950.00}]},{\"id\":26074,\"insuredApplyNo\":\"00200000603301120170082419\",\"insuredFormNo\":\"10200000603301120170008352\",\"insuranceTypeId\":2,\"cooperationModeId\":2,\"billSignTime\":1488955442000,\"insuredStartTime\":1488988800000,\"insuredFee\":6183.74,\"tax\":0.00,\"itemMsgList\":[{\"insuranceFormId\":26074,\"insuranceType\":2,\"insuranceCategoryCode\":\"033001\",\"insuranceName\":\"车损险\",\"insuranceFee\":3908.46},{\"insuranceFormId\":26074,\"insuranceType\":2,\"insuranceCategoryCode\":\"033002\",\"insuranceName\":\"第三者责任险\",\"insuranceFee\":1468.70}]}],\"packageMsg\":{\"packageOrderSn\":null,\"packageName\":\"C5000\",\"packagePrice\":0.02,\"packageFee\":1200.00,\"firstPayId\":null,\"firstPayNo\":null,\"firstPaidAmount\":null,\"gmtFirstPaid\":null,\"secondPayId\":null,\"secondPayNo\":null,\"secondPaidAmount\":null,\"gmtSecondPaid\":null},\"agentAccount\":null}";

        InsuranceSettleBasicMsg basicMsg = JsonUtil.strToObject(msg, InsuranceSettleBasicMsg.class);

        insuranceSettleCalculateBiz.calculateSettleData(basicMsg, true);

    }



    private List<InsuranceSettleFormBO> getFormBOList(String sn){
        List<InsuranceSettleFormBO> formBOList = new ArrayList<>();
        formBOList.add(getFormBO(sn+"formsn-1", 1));
        formBOList.add(getFormBO(sn+"formsn-2", 2));
        return formBOList;
    }
    private InsuranceSettleFormBO getFormBO(String sn, Integer typeId){
        InsuranceSettleFormBO formBO = new InsuranceSettleFormBO();
        formBO.setInsuredFormNo(sn);
        formBO.setInsuranceTypeId(typeId);
        formBO.setBillSignTime(new Date());
        formBO.setInsuredStartTime(new Date());
        formBO.setInsuredFee(new BigDecimal(20000));

        return formBO;
    }
    private InsuranceSettleBasicBO getBasicBO(String sn){
        InsuranceSettleBasicBO basicBO = new InsuranceSettleBasicBO();
        basicBO.setInsuranceOrderSn(sn);
        basicBO.setInsuranceCompanyId(2);
        basicBO.setCooperationModeId(CooperationModeEnum.GIVE_REWARD.getCode());
        basicBO.setVehicleSn("浙123456");
        basicBO.setAgentId(669749);
        basicBO.setAgentName("自动化云南新客户s");
        basicBO.setInsuredProvince("广东省");
        basicBO.setInsuredProvinceCode("440000");
        basicBO.setInsuredCity("广州市");
        basicBO.setInsuredCityCode("440100");

        basicBO.setFormBOList(getFormBOList(sn));
        return basicBO;
    }

    @Test
    public void test_calculateSettleDataExtend(){
        String orderSn = "hzt-test-order-sn-01";
        InsuranceSettleBasicBO basicBO = getBasicBO(orderSn);
        insuranceSettleCalculateExtendBiz.calculateSettleData(basicBO);
    }
}
