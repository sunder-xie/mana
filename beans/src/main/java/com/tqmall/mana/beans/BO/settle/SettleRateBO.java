package com.tqmall.mana.beans.BO.settle;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by huangzhangting on 17/2/20.
 */
@Data
public class SettleRateBO {
    private BigDecimal settleRate = BigDecimal.ZERO; //结算比例
    private Integer settleCondition; //结算条件
}
