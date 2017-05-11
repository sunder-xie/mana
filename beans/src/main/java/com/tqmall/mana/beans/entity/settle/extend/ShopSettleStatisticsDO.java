package com.tqmall.mana.beans.entity.settle.extend;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by huangzhangting on 17/1/21.
 */
@Data
public class ShopSettleStatisticsDO {
    private BigDecimal totalInsuredFee; //保费合计
    private BigDecimal totalSettleFee; //结算金额合计
    private BigDecimal totalServiceFee; //服务包工时费合计
    private BigDecimal totalCouponAmount; //券金额合计

    private BigDecimal totalCash; //应返现金合计：结算金额合计 - 服务包工时费合计

    public void calculateTotalCash(){
        if(totalSettleFee==null){
            return;
        }
        if(totalServiceFee==null){
            totalCash = totalSettleFee;
            return;
        }
        totalCash = totalSettleFee.subtract(totalServiceFee);
    }
}
