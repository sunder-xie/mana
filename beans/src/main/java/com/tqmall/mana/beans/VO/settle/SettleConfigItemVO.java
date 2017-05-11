package com.tqmall.mana.beans.VO.settle;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zhouheng on 17/1/13.
 */
@Data
public class SettleConfigItemVO {

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


    /** 分组信息 */
    private String groupName;

    private String insuranceCompanyName;

    private String insuranceTypeName;

    private String calculateModeName;

}
