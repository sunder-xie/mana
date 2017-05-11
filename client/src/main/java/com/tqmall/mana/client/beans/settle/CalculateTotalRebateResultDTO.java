package com.tqmall.mana.client.beans.settle;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by huangzhangting on 17/3/11.
 */
@Data
public class CalculateTotalRebateResultDTO implements Serializable {
    private BigDecimal totalRebate; //总返利
    private String formula; //计算公式
}
