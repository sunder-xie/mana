package com.tqmall.mana.beans.BO.coupon;

import lombok.Data;

/**
 * Created by huangzhangting on 16/12/7.
 */
@Data
public class SearchCouponSendLogBO {
    private Integer pageIndex;
    private Integer pageSize;

    private int offset; //数据库limit使用

    private String startDateStr;
    private String endDateStr;
    private String sendMobile;
    private Integer sendStatus;

}
