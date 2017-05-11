package com.tqmall.mana.beans.entity.cashcoupon;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CashCouponRuleConfigDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private BigDecimal onlineForcibleMinfee;

    private BigDecimal onlineCommercialMinfee;

    private Integer onlineFormValidateDays;

    private Integer onlineCooperationMode;

    private BigDecimal offlineForcibleMinfee;

    private BigDecimal offlineCommercialMinfee;

    private Integer offlineFormValidateDays;

    private Integer offlineCooperationMode;

    private BigDecimal faceAmount;

    private Integer cashCouponValidateDays;

    private Integer ruleStatus;

    private BigDecimal forcibleRebateRatio;

    private BigDecimal commercialRebateRatio;

}