package com.tqmall.mana.test.ext;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.param.settle.finance.WithdrawRewardPO;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.external.dubbo.finance.ExtFcInsuranceService;
import com.tqmall.mana.external.dubbo.insurance.ExtInsuranceCompanyService;
import com.tqmall.mana.test.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by huangzhangting on 17/2/17.
 */
@Slf4j
public class ExtFcInsuranceServiceTest extends BaseTest {
    @Autowired
    private ExtFcInsuranceService extFcInsuranceService;
    @Autowired
    private ExtInsuranceCompanyService extInsuranceCompanyService;

    @Test
    public void test(){
        WithdrawRewardPO withdrawRewardPO = new WithdrawRewardPO();
        withdrawRewardPO.setShopId(38644);
        withdrawRewardPO.setInsuranceNo("hzt-test");
        withdrawRewardPO.setWithdrawAmount(new BigDecimal(100));

        extFcInsuranceService.withdrawReward(withdrawRewardPO);

    }
    @Test
    public void Test(){

        log.info("#####  "+JsonUtil.objectToStr(extInsuranceCompanyService.allCompanyList()));
    }
}
