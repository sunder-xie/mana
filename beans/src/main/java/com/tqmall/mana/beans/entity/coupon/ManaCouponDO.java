package com.tqmall.mana.beans.entity.coupon;

import lombok.Data;

import java.util.Date;

@Data
public class ManaCouponDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private String couponContent;

    private Integer couponTypeId;

    private Integer couponStatus;

    private Date startDate;

    private Date endDate;

    private Integer isEncrypt; // 内容是否加密 0:否 1:是'

}