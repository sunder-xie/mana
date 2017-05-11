package com.tqmall.mana.beans.param.settle.finance;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by huangzhangting on 17/2/16.
 */
@Data
public class WithdrawRewardPO {
    private Integer shopId; //门店id
    private String insuranceNo; //保单号
    private BigDecimal withdrawAmount; //提现金额
}
