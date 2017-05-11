package com.tqmall.mana.biz.manager.settle.checkDetail;

import com.tqmall.mana.beans.BO.settle.SettleShopBO;

import java.math.BigDecimal;

/**
 * Created by zxg on 17/2/4.
 * 15:30
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public interface SettleShopBaseBiz {
/*
    //新增未结算现金,若不存在记录会自动新增数据
    Boolean addPayableCashAmount(BigDecimal payableCashAmount,Integer shopId);
    //未结算现金改为已结算,不得小于未结算的总额
    Boolean changeCashFromPayableToSettled(BigDecimal payableToSettledAmount,Integer shopId);

    //新增未结算奖励金,若不存在记录会自动新增数据
    Boolean addPayableBonusAmount(BigDecimal payableBonusAmount,Integer shopId);
    //未结算奖励金改为已结算
    Boolean changeBonusFromPayableToSettled(BigDecimal payableToSettledAmount,Integer shopId);

    //新增待生效的服务包数量,若不存在记录会自动新增数据
    Boolean addWaitEffectPackageNum(Integer waitEffectPackageNum,Integer shopId);
    //待生效 -> 待发货
    Boolean changePackageWaitEffectToWait(Integer waitEffectToWaitPackageNum,Integer shopId);
    //待发货->配送中的服务包数量
    Boolean changePackageWaitToSend(Integer waitToSendPackageNum,Integer shopId);
    //配送中->已签收的服务包数量
    Boolean changePackageSendToReceive(Integer sendToReceivePackageNum,Integer shopId);


    //根据门店id获得门店对账的基本信息
    SettleShopBO getShopByShopId(Integer shopId);
*/
}
