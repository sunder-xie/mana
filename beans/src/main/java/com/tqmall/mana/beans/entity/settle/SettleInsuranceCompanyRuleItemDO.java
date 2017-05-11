package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SettleInsuranceCompanyRuleItemDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private Integer settleRuleId;

    private Integer scenarioType;

    private Integer insuranceType;

    private BigDecimal commissionRate;

}