package com.tqmall.mana.beans.BO.customer;

import lombok.Data;

/**
 * 修改客户地址参数封装
 * Created by huangzhangting on 16/12/28.
 */
@Data
public class UpdateCustomerAddressBO {
    private Integer id;

    private Integer customerProvince;

    private Integer customerCity;

    private Integer customerDistrict;

    private String customerAddress;

}
