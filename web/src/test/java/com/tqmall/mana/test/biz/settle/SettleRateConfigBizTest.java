package com.tqmall.mana.test.biz.settle;

import com.tqmall.mana.beans.entity.settle.SettleRateConfigDO;
import com.tqmall.mana.beans.param.settle.RateConfigQueryPO;
import com.tqmall.mana.biz.manager.settle.config.SettleRateConfigBiz;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by huangzhangting on 17/3/27.
 */
public class SettleRateConfigBizTest extends BaseTest {
    @Autowired
    private SettleRateConfigBiz settleRateConfigBiz;


    @Test
    public void test_query(){
        RateConfigQueryPO queryPO = new RateConfigQueryPO();
        queryPO.setInsuranceCompanyId(1);
        queryPO.setCityCode("110000");

        List<SettleRateConfigDO> list = settleRateConfigBiz.getAll(queryPO);
        System.out.println(JsonUtil.objectToStr(list));
    }
}
