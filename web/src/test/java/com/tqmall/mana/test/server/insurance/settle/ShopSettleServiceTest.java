package com.tqmall.mana.test.server.insurance.settle;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.settle.ShopSettleDetailRewardModeBO;
import com.tqmall.mana.biz.manager.settle.settleCalculate.InsuranceSettleCalculateBiz;
import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettleBasicMsg;
import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettleMaterialAllowanceMsg;
import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettleMsg;
import com.tqmall.mana.biz.mq.insurance.settle.SettleMsgTypeEnum;
import com.tqmall.mana.client.beans.param.*;
import com.tqmall.mana.client.beans.settle.SettleShopDTO;
import com.tqmall.mana.client.beans.settle.ShopSettleDetailDTO;
import com.tqmall.mana.client.beans.settle.ShopSettleDetailPackageModeDTO;
import com.tqmall.mana.client.beans.settle.ShopSettleDetailRewardModeDTO;
import com.tqmall.mana.client.service.settle.RpcSettleServicePackageService;
import com.tqmall.mana.client.service.settle.RpcSettleShopService;
import com.tqmall.mana.component.enums.insurance.dict.AgentTagEnum;
import com.tqmall.mana.component.enums.insurance.dict.CooperationModeEnum;
import com.tqmall.mana.component.enums.insurance.dict.RewardStatusEnum;
import com.tqmall.mana.component.enums.insurance.dict.WithdrawCashStatusEnum;
import com.tqmall.mana.component.util.DateUtil;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by huangzhangting on 17/1/21.
 */
public class ShopSettleServiceTest extends BaseTest {
    @Autowired
    private RpcSettleShopService rpcSettleShopService;
    @Autowired
    private RpcSettleServicePackageService rpcSettleServicePackageService;


    @Test
    public void shopSettleDetailPageTest_奖励金(){
        ShopSettleDetailRewardModeQueryParam param = new ShopSettleDetailRewardModeQueryParam();
//        param.setInsuredFormNo("hzt_test_insuranceOrderSn_0005_1_2");
//        param.setInsuranceTypeId(InsuranceTypeEnum.BIZ_INSURANCE.getCode());
//        String dateStr = "2017-01-23 15:14:10";
//        param.setBillSignTimeEnd(DateUtil.stringToDate(dateStr, DateUtil.yyyy_MM_dd_HH_mm_ss));

//        param.setRewardStatus(RewardStatusEnum.HAS_EFFECTED.getCode());

//        param.setVehicleSn("粤TFB529");
        param.setIfUseCoupon(1);
        param.setAgentTag(AgentTagEnum.YUN_XIU.getCode());

        PagingResult<ShopSettleDetailRewardModeDTO> result = rpcSettleShopService.shopSettleDetailRewardModePage(param);
        System.out.println(JsonUtil.objectToStr(result));

    }

    @Test
    public void shopSettleDetailExtPageTest_买服务包送保险(){
        ShopSettleDetailPackageModeQueryParam param = new ShopSettleDetailPackageModeQueryParam();
        param.setCooperationModeId(CooperationModeEnum.GIVE_INSURANCE.getCode());
        param.setPageSize(10);

        PagingResult<ShopSettleDetailPackageModeDTO> result = rpcSettleShopService.shopSettleDetailPackageModePage(param);
        System.out.println(JsonUtil.objectToStr(result));
    }

    @Test
    public void shopSettleDetailExtPageTest_买保险送服务包(){
        ShopSettleDetailPackageModeQueryParam param = new ShopSettleDetailPackageModeQueryParam();
        param.setCooperationModeId(CooperationModeEnum.GIVE_PACKAGE.getCode());
        param.setPageSize(8);
        param.setAgentTag(AgentTagEnum.YUN_XIU.getCode());
//        param.setInsuredFormNo("hzt_test_insuranceOrderSn_0006_2_2");
//        param.setVehicleSn("粤B0A9A2");
        param.setApplyTimeStart(DateUtil.stringToDate("2017-03-15 12:26:57", DateUtil.yyyy_MM_dd_HH_mm_ss));

        PagingResult<ShopSettleDetailPackageModeDTO> result = rpcSettleShopService.shopSettleDetailPackageModePage(param);
        System.out.println(JsonUtil.objectToStr(result));
    }



    @Test
    public void shopSettleAuditTest(){
        SettleAuditParam param = new SettleAuditParam();
        param.setOperator("hzt-test");
        List<Integer> idList = new ArrayList<Integer>(){{
            add(20);
            add(24);
        }};
        param.setIdList(idList);

//        Result result = rpcSettleShopService.shopSettleAudit(param);
//        System.out.println(JsonUtil.objectToStr(result));
    }

    @Test
    public void shopSettleTest(){
        SettleParam param = new SettleParam();
        param.setOperator("hzt-test");
        List<Integer> idList = new ArrayList<Integer>(){{
            add(19);
//            add(24);
        }};
        param.setIdList(idList);

//        Result result = rpcSettleShopService.shopSettle(param);
//        System.out.println(JsonUtil.objectToStr(result));
    }

    @Test
    public void modifyServicePackageTest(){
        ServicePackageModifyParam param = new ServicePackageModifyParam();
        param.setOperator("hzt-test");
        param.setPackageStatus(2);
        List<String> orderSnList = new ArrayList<String>(){{
            add("hzt_test_packageOrderSn_0006");
            add("hzt_test_packageOrderSn_0007");
        }};
        param.setOrderSnList(orderSnList);

        Result result = rpcSettleServicePackageService.modifyServicePackage(param);
        System.out.println(JsonUtil.objectToStr(result));
    }


    @Test
    public void modifyWithdrawCashStatusTest(){
        RewardModifyParam param = new RewardModifyParam();
        param.setOperator("hzt");
        param.setIdList(new ArrayList<Integer>(){{
            add(46);
        }});
        param.setWithdrawCashStatus(WithdrawCashStatusEnum.CONFIRM_PAID.getCode());


//        Result result = rpcSettleShopService.modifyWithdrawCashStatus(param);
//        System.out.println(JsonUtil.objectToStr(result));
    }


    @Test
    public void test_modifyBalanceStatus(){
        BalanceStatusModifyParam param = new BalanceStatusModifyParam();
        param.setOperator("hzt_test");
        param.setStatus(5);
        param.setIdList(new ArrayList<Integer>(){{
            add(49);
        }});

        Result result = rpcSettleShopService.modifyBalanceStatus(param);
        System.out.println(JsonUtil.objectToStr(result));
    }





    @Autowired
    private InsuranceSettleCalculateBiz insuranceSettleCalculateBiz;

    private static final Lock SETTLE_DATA_LOCK = new ReentrantLock(true);

    //@Test
    public void testMqMsg(String msg){
//        msg = "{\"msgContent\":\"{\\\"agentId\\\":669749,\\\"agentName\\\":\\\"自动化电销云修\\\",\\\"applicantName\\\":\\\"杨林\\\",\\\"beAppliedName\\\":\\\"杨林\\\",\\\"cooperationModeId\\\":3,\\\"formMsgList\\\":[{\\\"billSignTime\\\":1487062246000,\\\"cooperationModeId\\\":3,\\\"id\\\":25856,\\\"insuranceTypeId\\\":2,\\\"insuredApplyNo\\\":\\\"00200000603301120170081620\\\",\\\"insuredFee\\\":0.01,\\\"insuredFormNo\\\":\\\"10200000603301120170008206\\\",\\\"insuredStartTime\\\":1487088000000,\\\"itemMsgList\\\":[{\\\"insuranceCategoryCode\\\":\\\"033001\\\",\\\"insuranceFee\\\":3650.55,\\\"insuranceFormId\\\":25856,\\\"insuranceName\\\":\\\"车损险\\\",\\\"insuranceType\\\":2},{\\\"insuranceCategoryCode\\\":\\\"033002\\\",\\\"insuranceFee\\\":1468.70,\\\"insuranceFormId\\\":25856,\\\"insuranceName\\\":\\\"第三者责任险\\\",\\\"insuranceType\\\":2},{\\\"insuranceCategoryCode\\\":\\\"033005\\\",\\\"insuranceFee\\\":1463.95,\\\"insuranceFormId\\\":25856,\\\"insuranceName\\\":\\\"全车盗抢险\\\",\\\"insuranceType\\\":2}],\\\"tax\\\":0.00},{\\\"billSignTime\\\":1487062246000,\\\"cooperationModeId\\\":3,\\\"id\\\":25857,\\\"insuranceTypeId\\\":1,\\\"insuredApplyNo\\\":\\\"00200000603000120170000975\\\",\\\"insuredFee\\\":0.01,\\\"insuredFormNo\\\":\\\"10200000603000120170000099\\\",\\\"insuredStartTime\\\":1487088000000,\\\"itemMsgList\\\":[{\\\"insuranceCategoryCode\\\":\\\"030000\\\",\\\"insuranceFee\\\":950.00,\\\"insuranceFormId\\\":25857,\\\"insuranceName\\\":\\\"交通事故责任强制保险\\\",\\\"insuranceType\\\":1}],\\\"tax\\\":660.00}],\\\"insuranceCompanyId\\\":1,\\\"insuranceOrderSn\\\":\\\"EC1603000320170214160024lHHx574\\\",\\\"insuredCity\\\":\\\"珠海市\\\",\\\"insuredCityCode\\\":\\\"440400\\\",\\\"insuredProvince\\\":\\\"广东省\\\",\\\"insuredProvinceCode\\\":\\\"440000\\\",\\\"packageMsg\\\":{\\\"packageFee\\\":1020.00,\\\"packageName\\\":\\\"2C5000\\\",\\\"packageOrderSn\\\":\\\"B17021477081124\\\",\\\"packagePrice\\\":0.02},\\\"vehicleSn\\\":\\\"粤O14567\\\"}\",\"msgType\":1}";
//
//        msg = "{\"msgContent\":\"{\\\"agentId\\\":669749,\\\"agentName\\\":\\\"自动化云南新客户s\\\",\\\"applicantName\\\":\\\"杨林\\\",\\\"beAppliedName\\\":\\\"杨林\\\",\\\"cooperationModeId\\\":2,\\\"formMsgList\\\":[{\\\"billSignTime\\\":1486848347000,\\\"cooperationModeId\\\":2,\\\"id\\\":25861,\\\"insuranceTypeId\\\":1,\\\"insuredApplyNo\\\":\\\"0\\\",\\\"insuredFee\\\":100.00,\\\"insuredFormNo\\\":\\\"987654321\\\",\\\"insuredStartTime\\\":1487001600000,\\\"itemMsgList\\\":[{\\\"insuranceCategoryCode\\\":\\\"030000\\\",\\\"insuranceFee\\\":100.00,\\\"insuranceFormId\\\":25861,\\\"insuranceName\\\":\\\"交通事故责任强制保险\\\",\\\"insuranceType\\\":1}],\\\"tax\\\":0.00},{\\\"billSignTime\\\":1486848347000,\\\"cooperationModeId\\\":2,\\\"id\\\":25862,\\\"insuranceTypeId\\\":2,\\\"insuredApplyNo\\\":\\\"0\\\",\\\"insuredFee\\\":60.00,\\\"insuredFormNo\\\":\\\"123456789\\\",\\\"insuredStartTime\\\":1486828800000,\\\"itemMsgList\\\":[{\\\"insuranceCategoryCode\\\":\\\"033001\\\",\\\"insuranceFee\\\":10.00,\\\"insuranceFormId\\\":25862,\\\"insuranceName\\\":\\\"车损险\\\",\\\"insuranceType\\\":2},{\\\"insuranceCategoryCode\\\":\\\"033002\\\",\\\"insuranceFee\\\":20.00,\\\"insuranceFormId\\\":25862,\\\"insuranceName\\\":\\\"第三者责任险\\\",\\\"insuranceType\\\":2},{\\\"insuranceCategoryCode\\\":\\\"033005\\\",\\\"insuranceFee\\\":30.00,\\\"insuranceFormId\\\":25862,\\\"insuranceName\\\":\\\"全车盗抢险\\\",\\\"insuranceType\\\":2}],\\\"tax\\\":0.00}],\\\"insuranceCompanyId\\\":1,\\\"insuranceOrderSn\\\":\\\"EC16030003201702150842567262072\\\",\\\"insuredCity\\\":\\\"珠海市\\\",\\\"insuredCityCode\\\":\\\"440400\\\",\\\"insuredProvince\\\":\\\"广东省\\\",\\\"insuredProvinceCode\\\":\\\"440000\\\",\\\"packageMsg\\\":{\\\"packageFee\\\":750.00,\\\"packageName\\\":\\\"C5000\\\",\\\"packagePrice\\\":3536.00},\\\"vehicleSn\\\":\\\"粤Y12345\\\"}\",\"msgType\":1}";

        InsuranceSettleMsg settleMsg = JsonUtil.strToObject(msg, InsuranceSettleMsg.class);
        if(settleMsg==null){
            return;
        }

        Integer msgType = settleMsg.getMsgType();
        if(SettleMsgTypeEnum.FORM_BASIC.getCode().equals(msgType)) {
            InsuranceSettleBasicMsg basicMsg =
                    JsonUtil.strToObject(settleMsg.getMsgContent(), InsuranceSettleBasicMsg.class);
//            insuranceSettleCalculateBiz.lockCalculateSettleData(basicMsg);

            SETTLE_DATA_LOCK.lock();
            try {
                insuranceSettleCalculateBiz.calculateSettleData(basicMsg, true);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                SETTLE_DATA_LOCK.unlock();
            }


        }else if(SettleMsgTypeEnum.MATERIAL_ALLOWANCE.getCode().equals(msgType)){
            InsuranceSettleMaterialAllowanceMsg materialAllowanceMsg =
                    JsonUtil.strToObject(settleMsg.getMsgContent(), InsuranceSettleMaterialAllowanceMsg.class);
            insuranceSettleCalculateBiz.calculateSettleDataForMaterialAllowance(materialAllowanceMsg);

        }

    }


    @Test
    public void test11(){
        List<String> msgList = new ArrayList<>();
        msgList.add("{\"msgContent\":\"{\\\"agentId\\\":669749,\\\"agentName\\\":\\\"自动化电销云修\\\",\\\"applicantName\\\":\\\"nina\\\",\\\"beAppliedName\\\":\\\"nina\\\",\\\"cooperationModeId\\\":2,\\\"formMsgList\\\":[{\\\"billSignTime\\\":1487835120000,\\\"cooperationModeId\\\":2,\\\"id\\\":25999,\\\"insuranceTypeId\\\":1,\\\"insuredApplyNo\\\":\\\"00200000603000120170001137\\\",\\\"insuredFee\\\":3850.00,\\\"insuredFormNo\\\":\\\"10200000603000120170000134\\\",\\\"insuredStartTime\\\":1487865600000,\\\"itemMsgList\\\":[{\\\"insuranceCategoryCode\\\":\\\"030000\\\",\\\"insuranceFee\\\":1100.00,\\\"insuranceFormId\\\":25999,\\\"insuranceName\\\":\\\"交通事故责任强制保险\\\",\\\"insuranceType\\\":1}],\\\"tax\\\":2750.00},{\\\"cooperationModeId\\\":2,\\\"id\\\":26000,\\\"insuranceTypeId\\\":2,\\\"insuredApplyNo\\\":\\\"00200000603301120170081845\\\",\\\"insuredFee\\\":2607.41,\\\"insuredStartTime\\\":1487865600000,\\\"itemMsgList\\\":[{\\\"insuranceCategoryCode\\\":\\\"033005\\\",\\\"insuranceFee\\\":2172.84,\\\"insuranceFormId\\\":26000,\\\"insuranceName\\\":\\\"全车盗抢险\\\",\\\"insuranceType\\\":2}],\\\"tax\\\":0.00}],\\\"insuranceCompanyId\\\":1,\\\"insuranceOrderSn\\\":\\\"EC160300032017022315252967R2138\\\",\\\"insuredCity\\\":\\\"韶关市\\\",\\\"insuredCityCode\\\":\\\"440200\\\",\\\"insuredProvince\\\":\\\"广东省\\\",\\\"insuredProvinceCode\\\":\\\"440000\\\",\\\"packageMsg\\\":{\\\"packageFee\\\":500.00,\\\"packageName\\\":\\\"B2500\\\",\\\"packagePrice\\\":2538.00},\\\"vehicleSn\\\":\\\"粤022329\\\"}\",\"msgType\":1}");
        msgList.add("{\"msgContent\":\"{\\\"agentId\\\":669749,\\\"agentName\\\":\\\"自动化电销云修\\\",\\\"applicantName\\\":\\\"nina\\\",\\\"beAppliedName\\\":\\\"nina\\\",\\\"cooperationModeId\\\":2,\\\"formMsgList\\\":[{\\\"billSignTime\\\":1487835120000,\\\"cooperationModeId\\\":2,\\\"id\\\":25999,\\\"insuranceTypeId\\\":1,\\\"insuredApplyNo\\\":\\\"00200000603000120170001137\\\",\\\"insuredFee\\\":3850.00,\\\"insuredFormNo\\\":\\\"10200000603000120170000134\\\",\\\"insuredStartTime\\\":1487865600000,\\\"itemMsgList\\\":[{\\\"insuranceCategoryCode\\\":\\\"030000\\\",\\\"insuranceFee\\\":1100.00,\\\"insuranceFormId\\\":25999,\\\"insuranceName\\\":\\\"交通事故责任强制保险\\\",\\\"insuranceType\\\":1}],\\\"tax\\\":2750.00},{\\\"billSignTime\\\":1487835120000,\\\"cooperationModeId\\\":2,\\\"id\\\":26000,\\\"insuranceTypeId\\\":2,\\\"insuredApplyNo\\\":\\\"00200000603301120170081845\\\",\\\"insuredFee\\\":2607.41,\\\"insuredFormNo\\\":\\\"10200000603301120170008254\\\",\\\"insuredStartTime\\\":1487865600000,\\\"itemMsgList\\\":[{\\\"insuranceCategoryCode\\\":\\\"033005\\\",\\\"insuranceFee\\\":2172.84,\\\"insuranceFormId\\\":26000,\\\"insuranceName\\\":\\\"全车盗抢险\\\",\\\"insuranceType\\\":2}],\\\"tax\\\":0.00}],\\\"insuranceCompanyId\\\":1,\\\"insuranceOrderSn\\\":\\\"EC160300032017022315252967R2138\\\",\\\"insuredCity\\\":\\\"韶关市\\\",\\\"insuredCityCode\\\":\\\"440200\\\",\\\"insuredProvince\\\":\\\"广东省\\\",\\\"insuredProvinceCode\\\":\\\"440000\\\",\\\"packageMsg\\\":{\\\"packageFee\\\":500.00,\\\"packageName\\\":\\\"B2500\\\",\\\"packagePrice\\\":2538.00},\\\"vehicleSn\\\":\\\"粤022329\\\"}\",\"msgType\":1}");

        for(String msg : msgList){
            testMqMsg(msg);
        }

    }



    @Test
    public void test_statistics(){
        Integer shopId = 324846;
        Result<SettleShopDTO> result = rpcSettleShopService.getSettleShopDetailByShopId(shopId);
        System.out.println(JsonUtil.objectToStr(result));
    }
}
