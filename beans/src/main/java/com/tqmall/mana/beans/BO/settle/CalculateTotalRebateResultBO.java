package com.tqmall.mana.beans.BO.settle;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by huangzhangting on 17/3/11.
 */
@Data
public class CalculateTotalRebateResultBO {
    private BigDecimal totalRebate; //总返利
    private String formula; //计算公式
}
