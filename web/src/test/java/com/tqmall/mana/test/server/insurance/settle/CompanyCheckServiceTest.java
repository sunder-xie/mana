package com.tqmall.mana.test.server.insurance.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.client.beans.param.SettleCompanyCheckDetailParam;
import com.tqmall.mana.client.service.settle.RpcSettleCompanyCheckService;
import com.tqmall.mana.component.enums.insurance.dict.AgentTagEnum;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhangting on 17/3/23.
 */
public class CompanyCheckServiceTest extends BaseTest {
    @Autowired
    private RpcSettleCompanyCheckService rpcSettleCompanyCheckService;

    @Test
    public void test_updateConfirmMoneyStatusByIds(){
        String operator = "hzt";
        List<Integer> idList = new ArrayList<>();
        idList.add(24);

        rpcSettleCompanyCheckService.updateConfirmMoneyStatusByIds(idList, operator);

    }

    @Test
    public void test_insuranceCompanyCheckForERP(){
        SettleCompanyCheckDetailParam param = new SettleCompanyCheckDetailParam();
        param.setPageSize(10);
        param.setAgentTag(AgentTagEnum.YUN_XIU.getCode());
        Result result = rpcSettleCompanyCheckService.insuranceCompanyCheckForERP(param);
        System.out.println(JsonUtil.objectToStr(result));
    }
}
