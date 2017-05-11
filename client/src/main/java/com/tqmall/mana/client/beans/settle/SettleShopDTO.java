package com.tqmall.mana.client.beans.settle;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SettleShopDTO implements Serializable {
    //主键ID
    private Integer id;

    //门店id
    private Integer agentId;

    //已结算的现金
    private BigDecimal settledCashAmount;

    //未结算的现金
    private BigDecimal payableCashAmount;

    //已结算的奖励金
    private BigDecimal settledBonusAmount;

    //未结算的奖励金
    private BigDecimal payableBonusAmount;

    //待生效的服务包数量
    private Integer waitEffectPackageNum;

    //待发货的服务包数量
    private Integer waitPackageNum;

    //配送中的服务包数量
    private Integer sendPackageNum;

    //已签收的服务包数量
    private Integer receivePackageNum;
}