package com.tqmall.mana.beans.BO.coupon;

import lombok.Data;

/**
 * Created by huangzhangting on 16/12/6.
 */
@Data
public class SendCouponBO {
    private String mobiles; //手机号，多个值以逗号隔开
    private Integer couponType;
    private Integer couponNum;
    private String smsTemplateKey; //短信模板key
}
