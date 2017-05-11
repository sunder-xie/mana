package com.tqmall.mana.beans.entity.coupon;

import lombok.Data;

import java.util.Date;

@Data
public class ManaCouponSendLogDO {
    private Integer id;

    private Date gmtCreate;

    private String creator;

    private String sendDate;

    private String sendContent;

    private Integer sendStatus;

    private String sendMobile;

    private Integer couponNum;

    private Integer couponTypeId;

    private String gmtCreateStr;

    private Integer isEncrypt; // 内容是否加密 0:否 1:是'

    private String templateKey; //短信模板key，前端使用是会赋值为短信描述，因为key没用，省字段
    private String templateContent; //短信模板内容，前端使用

}