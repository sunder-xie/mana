package com.tqmall.mana.test.biz.settle;

import com.google.common.collect.Lists;
import com.tqmall.mana.beans.param.settle.BalanceStatusModifyPO;
import com.tqmall.mana.beans.param.settle.SettleFeeModifyPO;
import com.tqmall.mana.biz.manager.settle.checkDetail.SettleShopCheckBiz;
import com.tqmall.mana.component.enums.insurance.dict.BalanceStatusEnum;
import com.tqmall.mana.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by huangzhangting on 17/3/6.
 */
public class SettleShopCheckBizTest extends BaseTest {
    @Autowired
    private SettleShopCheckBiz settleShopCheckBiz;

    @Test
    public void testBiz(){
        String operator = "hzt";
        String insuredFormSn = "10200000603301120170000210";
        Integer formulaConfigId = 3;

    }

    @Test
    public void unUseCashCoupon_test(){
        SettleFeeModifyPO modifyPO = new SettleFeeModifyPO();
        modifyPO.setInsuranceOrderSn("AEC16110001201702181752347T02434");
        modifyPO.setBizInsuranceRate(new BigDecimal(0.37));
        modifyPO.setForceInsuranceRate(new BigDecimal(0.08));

        settleShopCheckBiz.unUseCashCoupon(modifyPO);
    }

    @Test
    public void test_modifyBalanceStatus(){
        BalanceStatusModifyPO modifyPO = new BalanceStatusModifyPO();
        modifyPO.setOperator("hzt");
        modifyPO.setIdList(Lists.newArrayList(822,823));
        modifyPO.setStatus(BalanceStatusEnum.CONFIRM_PAID.getCode());

        settleShopCheckBiz.modifyBalanceStatus(modifyPO);
    }

}
