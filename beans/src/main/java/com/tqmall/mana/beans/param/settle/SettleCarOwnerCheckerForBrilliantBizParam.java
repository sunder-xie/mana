package com.tqmall.mana.beans.param.settle;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by jinju.zeng on 17/2/6.
 */
@Data
public class SettleCarOwnerCheckerForBrilliantBizParam {
    //淘气内部订单号
    private String insuranceOrderSn;

    //商业应交保费
    private BigDecimal syPayableInsuredFee;

    //交强应交保费
    private BigDecimal jqPayableInsuredFee;

    //车船税
    private BigDecimal texFee;
}
