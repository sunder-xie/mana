package com.tqmall.mana.web.test;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.biz.manager.settle.checkDetail.SettleShopBaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Created by zxg on 17/2/4.
 * 17:54
 * no bug,以后改代码的哥们，祝你好运~！！
 */

@RestController
@RequestMapping("testLock")
public class TestLockRestController {
    @Autowired
    private SettleShopBaseBiz settleShopBaseBiz;

    @RequestMapping(value = "addCash", method = RequestMethod.GET)
    public Result addPayableCashAmount(Integer shopId){

//        Boolean result = settleShopBaseBiz.addPayableCashAmount(new BigDecimal("120"), shopId);
        return Result.wrapSuccessfulResult(null);
    }

    @RequestMapping(value = "changeCash", method = RequestMethod.GET)
    public Result changeCashFromPayableToSettled(Integer shopId){

//        Boolean result = settleShopBaseBiz.changeCashFromPayableToSettled(new BigDecimal("20"), shopId);
        return Result.wrapSuccessfulResult(null);
    }
}
