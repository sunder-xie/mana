package com.tqmall.mana.beans.BO.cashcoupon;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CashCouponGoodsConfigBO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private Integer ruleConfigId;

    private Integer goodsType;

    private BigDecimal goodsCoefficient;

}