package com.tqmall.mana.web.test;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.biz.manager.settle.settleCalculate.InsuranceSettleCalculateBiz;
import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettleBasicMsg;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by huangzhangting on 17/2/23.
 */
@Controller
@RequestMapping("testInsuranceSettle")
public class TestInsuranceSettleController {
    private static Integer TEST_SHOP_ID = 152722;

    @Autowired
    private InsuranceSettleCalculateBiz insuranceSettleCalculateBiz;


    @RequestMapping("addMode3Data")
    @ResponseBody
    public Result addMode3Data(String no, Integer shopId){
        String insuranceOrderSn = "testmode3-"+no;
        String packageOrderSn = "ordersn"+no;
        String insuredFormNo_1 = "mode3no1"+no;
        String insuredFormNo_2 = "mode3no2"+no;

        String msg = "{\"cooperationModeId\":3,\"insuranceOrderSn\":\""+insuranceOrderSn+"\",\"applicantName\":\"薛翔睿\",\"beAppliedName\":\"薛翔睿\",\"vehicleSn\":\"粤A22222\",\"agentId\":152722,\"agentName\":\"小和山测试不送修理厂\",\"insuranceCompanyId\":1,\"insuredProvinceCode\":\"440000\",\"insuredCityCode\":\"440100\",\"insuredProvince\":\"广东省\",\"insuredCity\":\"广州市\",\"formMsgList\":[{\"id\":655,\"insuredApplyNo\":\"00200000603301120170081796\",\"insuredFormNo\":\""+insuredFormNo_2+"\",\"insuranceTypeId\":2,\"cooperationModeId\":3,\"billSignTime\":1487831368000,\"insuredStartTime\":1493308800000,\"insuredFee\":0.02,\"tax\":0.00,\"itemMsgList\":[{\"insuranceFormId\":655,\"insuranceType\":2,\"insuranceCategoryCode\":\"033001\",\"insuranceName\":\"车损险\",\"insuranceFee\":5895.03},{\"insuranceFormId\":655,\"insuranceType\":2,\"insuranceCategoryCode\":\"033002\",\"insuranceName\":\"第三者责任险\",\"insuranceFee\":1284.40},{\"insuranceFormId\":655,\"insuranceType\":2,\"insuranceCategoryCode\":\"033005\",\"insuranceName\":\"全车盗抢险\",\"insuranceFee\":2148.36},{\"insuranceFormId\":655,\"insuranceType\":2,\"insuranceCategoryCode\":\"033006\",\"insuranceName\":\"玻璃单独破碎险\",\"insuranceFee\":0.00},{\"insuranceFormId\":655,\"insuranceType\":2,\"insuranceCategoryCode\":\"033007\",\"insuranceName\":\"自燃损失险\",\"insuranceFee\":549.64},{\"insuranceFormId\":655,\"insuranceType\":2,\"insuranceCategoryCode\":\"033012\",\"insuranceName\":\"车损无法找到第三方特约险\",\"insuranceFee\":147.38}]}],\"packageMsg\":{\"packageOrderSn\":\""+packageOrderSn+"\",\"packageName\":\"G5000\",\"packagePrice\":0.02,\"packageFee\":950.00,\"firstPayId\":null,\"firstPayNo\":null,\"firstPaidAmount\":null,\"gmtFirstPaid\":null,\"secondPayId\":null,\"secondPayNo\":null,\"secondPaidAmount\":null,\"gmtSecondPaid\":null},\"agentAccount\":null}";
        InsuranceSettleBasicMsg basicMsg = JsonUtil.strToObject(msg, InsuranceSettleBasicMsg.class);
        if(shopId==null) {
            shopId = TEST_SHOP_ID;
        }
        basicMsg.setAgentId(shopId);

        insuranceSettleCalculateBiz.calculateSettleData(basicMsg, true);

        return ResultUtil.successResult("淘汽保单号："+insuranceOrderSn+"  门店id："+shopId);
    }

    @RequestMapping("addMode1Data")
    @ResponseBody
    public Result addMode1Data(String no, Integer shopId){
        String insuranceOrderSn = "testmode1-"+no;
        String insuredFormNo_1 = "mode1no1"+no;
        String insuredFormNo_2 = "mode1no2"+no;

        String msg = "{\"cooperationModeId\":1,\"insuranceOrderSn\":\""+insuranceOrderSn+"\",\"applicantName\":\"交商同\",\"beAppliedName\":\"交商同\",\"vehicleSn\":\"粤O12346\",\"agentId\":295106,\"agentName\":\"测试\",\"insuranceCompanyId\":1,\"insuredProvinceCode\":\"440000\",\"insuredCityCode\":\"440200\",\"insuredProvince\":\"广东省\",\"insuredCity\":\"韶关市\",\"formMsgList\":[{\"id\":631,\"insuredApplyNo\":\"00200000603000120170001094\",\"insuredFormNo\":\""+insuredFormNo_1+"\",\"insuranceTypeId\":1,\"cooperationModeId\":1,\"billSignTime\":1487755385000,\"insuredStartTime\":1487865600000,\"insuredFee\":2600.00,\"tax\":1650.00,\"itemMsgList\":[{\"insuranceFormId\":631,\"insuranceType\":1,\"insuranceCategoryCode\":\"030000\",\"insuranceName\":\"交通事故责任强制保险\",\"insuranceFee\":950.00}]},{\"id\":632,\"insuredApplyNo\":\"00200000603301120170081768\",\"insuredFormNo\":\""+insuredFormNo_2+"\",\"insuranceTypeId\":2,\"cooperationModeId\":1,\"billSignTime\":1487755385000,\"insuredStartTime\":1488124800000,\"insuredFee\":11973.99,\"tax\":0.00,\"itemMsgList\":[{\"insuranceFormId\":632,\"insuranceType\":2,\"insuranceCategoryCode\":\"033001\",\"insuranceName\":\"车损险\",\"insuranceFee\":5679.34},{\"insuranceFormId\":632,\"insuranceType\":2,\"insuranceCategoryCode\":\"033002\",\"insuranceName\":\"第三者责任险\",\"insuranceFee\":1468.70},{\"insuranceFormId\":632,\"insuranceType\":2,\"insuranceCategoryCode\":\"033005\",\"insuranceName\":\"全车盗抢险\",\"insuranceFee\":1929.45},{\"insuranceFormId\":632,\"insuranceType\":2,\"insuranceCategoryCode\":\"033003\",\"insuranceName\":\"车上人员责任险(司机)\",\"insuranceFee\":39.90},{\"insuranceFormId\":632,\"insuranceType\":2,\"insuranceCategoryCode\":\"033004\",\"insuranceName\":\"车上人员责任险(乘客)\",\"insuranceFee\":102.60},{\"insuranceFormId\":632,\"insuranceType\":2,\"insuranceCategoryCode\":\"033006\",\"insuranceName\":\"玻璃单独破碎险\",\"insuranceFee\":741.00},{\"insuranceFormId\":632,\"insuranceType\":2,\"insuranceCategoryCode\":\"033007\",\"insuranceName\":\"自燃损失险\",\"insuranceFee\":444.60}]}],\"packageMsg\":null,\"agentAccount\":null}";
        InsuranceSettleBasicMsg basicMsg = JsonUtil.strToObject(msg, InsuranceSettleBasicMsg.class);
        if(shopId==null) {
            shopId = TEST_SHOP_ID;
        }
        basicMsg.setAgentId(shopId);

        insuranceSettleCalculateBiz.calculateSettleData(basicMsg, true);

        return ResultUtil.successResult("淘汽保单号："+insuranceOrderSn+"  门店id："+shopId);
    }


}
