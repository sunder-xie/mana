package com.tqmall.mana.beans.BO.coupon;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by huangzhangting on 16/12/7.
 */
@Data
public class UpdateCouponBO {
    private String modifier;
    private Date gmtModified;
    private Integer couponStatus;
    private List<Integer> idList;
}
