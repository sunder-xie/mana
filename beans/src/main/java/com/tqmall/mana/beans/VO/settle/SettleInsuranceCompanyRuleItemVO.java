package com.tqmall.mana.beans.VO.settle;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by admin on 17/1/14.
 */
@Data
public class SettleInsuranceCompanyRuleItemVO {

    private Integer scenarioType;

    private String scenarioName;

    private Integer insuranceType;

    private String insuranceName;

    private BigDecimal commissionRate = BigDecimal.ZERO;
}
