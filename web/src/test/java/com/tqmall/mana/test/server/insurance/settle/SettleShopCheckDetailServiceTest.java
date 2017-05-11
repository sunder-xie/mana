package com.tqmall.mana.test.server.insurance.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.client.beans.otherInsurance.CheckAllowanceDTO;
import com.tqmall.mana.client.beans.param.CheckAllowanceListRequestParam;
import com.tqmall.mana.client.service.settle.RpcSettleShopCheckDetailService;
import com.tqmall.mana.component.enums.insurance.dict.AgentTagEnum;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huangzhangting on 17/4/12.
 */
public class SettleShopCheckDetailServiceTest extends BaseTest {
    @Autowired
    private RpcSettleShopCheckDetailService rpcSettleShopCheckDetailService;

    @Test
    public void test_query(){
        CheckAllowanceListRequestParam param = new CheckAllowanceListRequestParam();
        param.setPageSize(5);
        param.setAgentTag(AgentTagEnum.YUN_XIU.getCode());
        Result<CheckAllowanceDTO> result = rpcSettleShopCheckDetailService.checkAllowanceList(param);
        System.out.println(JsonUtil.objectToStr(result));
    }
}
