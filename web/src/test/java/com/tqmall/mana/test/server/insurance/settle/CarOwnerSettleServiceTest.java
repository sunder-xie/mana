package com.tqmall.mana.test.server.insurance.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.client.beans.param.SettleCarOwnerCheckerParam;
import com.tqmall.mana.client.beans.settle.SettleCarOwnerPagingResult;
import com.tqmall.mana.client.service.settle.RpcSettleCarOwnerCheckerService;
import com.tqmall.mana.component.enums.insurance.dict.AgentTagEnum;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huangzhangting on 17/3/3.
 */
public class CarOwnerSettleServiceTest extends BaseTest {
    @Autowired
    private RpcSettleCarOwnerCheckerService carOwnerCheckerService;

    @Test
    public void testQuery(){
        SettleCarOwnerCheckerParam param = new SettleCarOwnerCheckerParam();
        param.setIsTqmallPayStatus(1);
        param.setVehicleSn("粤AV441V-new");
        param.setAgentTag(AgentTagEnum.YUN_XIU.getCode());

        Result<SettleCarOwnerPagingResult> result = carOwnerCheckerService.carOwnerCheckerForERP(param);
        System.out.println(JsonUtil.objectToStr(result));
    }


    @Test
    public void testAudit(){
        Integer id = 107;
        String operator = "hzt";

        Result result = carOwnerCheckerService.reviewPayStatus(id, operator);
        System.out.println(JsonUtil.objectToStr(result));
    }

}
