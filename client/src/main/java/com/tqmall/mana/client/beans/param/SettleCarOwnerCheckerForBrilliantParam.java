package com.tqmall.mana.client.beans.param;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by jinju.zeng on 17/2/6.
 */
@Getter
@Setter
public class SettleCarOwnerCheckerForBrilliantParam implements Serializable {
    //淘气内部订单号
    private String insuranceOrderSn;

    //商业应交保费
    private BigDecimal syPayableInsuredFee;

    //交强应交保费
    private BigDecimal jqPayableInsuredFee;

    //车船税
    private BigDecimal texFee;
}
