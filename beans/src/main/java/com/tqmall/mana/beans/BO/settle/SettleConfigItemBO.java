package com.tqmall.mana.beans.BO.settle;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zhouheng on 17/1/13.
 */
@Data
public class SettleConfigItemBO {

    /**
     * 开始值
     */
    private BigDecimal itemStartValue;
    /**
     * 结束值
     */
    private BigDecimal itemEndValue;
    /**
     * 比率
     */
    private BigDecimal itemRate;
}
