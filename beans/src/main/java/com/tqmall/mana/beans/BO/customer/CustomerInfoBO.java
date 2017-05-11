package com.tqmall.mana.beans.BO.customer;

import lombok.Data;

/**
 * Created by huangzhangting on 16/12/28.
 */
@Data
public class CustomerInfoBO {
    private Integer id;

    private String customerMobile;

    private String customerName;

    private Integer customerSource;

    private String certificateType;

    private String certificateNo;

    //地址对象
    private CustomerAddressBO addressBO;

}
