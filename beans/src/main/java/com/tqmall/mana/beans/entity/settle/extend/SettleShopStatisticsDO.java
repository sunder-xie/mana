package com.tqmall.mana.beans.entity.settle.extend;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by huangzhangting on 17/3/6.
 */
@Data
public class SettleShopStatisticsDO {

    //已结算的现金
    private BigDecimal settledCashAmount;

    //未结算的现金
    private BigDecimal payableCashAmount;


    //待生效的服务包数量
    private Integer waitEffectPackageNum;

    //待发货的服务包数量
    private Integer waitPackageNum;

    //配送中的服务包数量
    private Integer sendPackageNum;

    //已签收的服务包数量
    private Integer receivePackageNum;

}
