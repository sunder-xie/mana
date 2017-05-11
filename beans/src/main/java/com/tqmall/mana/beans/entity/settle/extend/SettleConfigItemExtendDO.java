package com.tqmall.mana.beans.entity.settle.extend;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by huangzhangting on 17/2/22.
 */
@Data
public class SettleConfigItemExtendDO {
    private Integer id;

    private Integer basicId;

    private BigDecimal itemStartValue;

    private BigDecimal itemEndValue;

    private BigDecimal itemRate;


    /** 分组信息 */
    private String groupName;

    private String insuranceCompanyName;

    private Integer insuranceType;

    private Integer calculateMode;

}
