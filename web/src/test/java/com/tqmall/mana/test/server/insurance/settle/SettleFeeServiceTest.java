package com.tqmall.mana.test.server.insurance.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.client.beans.param.SettleFeeCalculateParam;
import com.tqmall.mana.client.beans.param.SettleFeeModifyParam;
import com.tqmall.mana.client.beans.param.UseCouponParam;
import com.tqmall.mana.client.beans.settle.CalculateTotalRebateResultDTO;
import com.tqmall.mana.client.service.settle.RpcSettleFeeService;
import com.tqmall.mana.component.bean.ConstantBean;
import com.tqmall.mana.component.enums.FormulaKeyEnum;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by huangzhangting on 17/3/8.
 */
public class SettleFeeServiceTest extends BaseTest {
    @Autowired
    private RpcSettleFeeService rpcSettleFeeService;


    @Test
    public void test_calculateTotalRebate(){
        SettleFeeCalculateParam param = new SettleFeeCalculateParam();
        param.setFormulaKey(FormulaKeyEnum.ALL_REBATE.getKey());
        param.setCouponAmount(new BigDecimal(500));
        param.setBizInsuranceFee(new BigDecimal(2000));
        param.setForceInsuranceFee(new BigDecimal(900));
        param.setInsuranceCompanyId(ConstantBean.AN_XIN_INSURANCE_ID);
        param.setCityCode("110000");

//        Result<CalculateTotalRebateResultDTO> result = rpcSettleFeeService.calculateTotalRebate(param);
//        System.out.println(result);
    }

    @Test
    public void test_modifyTotalRebate(){
        SettleFeeModifyParam param = new SettleFeeModifyParam();
        param.setInsuranceOrderSn("AEC16110001201702181752347T02434");
        param.setCouponSn("coupon_sn_005");
        param.setCouponAmount(new BigDecimal(500));
        param.setCouponCashTime(new Date());

//        Result result = rpcSettleFeeService.modifyTotalRebate(param);
//        System.out.println(JsonUtil.objectToStr(result));
    }

    @Test
    public void test_useCashCoupon(){
        UseCouponParam param = new UseCouponParam();
        param.setInsuranceOrderSn("AEC1611000120161226230530P911184");
        param.setCouponSn("coupon_sn_005");
        param.setCouponAmount(new BigDecimal(500));
        param.setCouponCashTime(new Date());
        param.setInsuranceCompanyId(ConstantBean.AN_XIN_INSURANCE_ID);
        param.setCityCode("321");
        param.setBizInsuranceFormulaKey(FormulaKeyEnum.BIZ_INSURANCE.getKey());
        param.setForceInsuranceFormulaKey(FormulaKeyEnum.FORCE_INSURANCE.getKey());

        Result result = rpcSettleFeeService.useCashCoupon(param);
        System.out.println(JsonUtil.objectToStr(result));
    }
}
