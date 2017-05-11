package com.tqmall.mana.beans.BO.cashcoupon;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhouheng on 17/4/11.
 */
@Data
public class CashCouponRegionConfigBO {

    private Integer id;

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
