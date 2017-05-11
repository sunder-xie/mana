package com.tqmall.mana.beans.BO.cashcoupon;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouheng on 17/4/11.
 */
@Data
public class CashCouponRuleConfigBO {

    private Integer id;

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

    private List<CashCouponGoodsConfigBO> goodsConfigBOList;

    private List<CashCouponRegionConfigBO> regionConfigBOList;
}
