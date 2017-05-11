package com.tqmall.mana.beans.entity.cashcoupon;

import lombok.Data;

import java.util.Date;

@Data
public class CashCouponRegionConfigDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private Integer ruleConfigId;

    private String provinceCode;

    private String provinceName;

    private String cityCode;

    private String cityName;

}