package com.tqmall.mana.beans.BO.customer;

import lombok.Data;

/**
 * 客户地址
 * Created by huangzhangting on 16/12/28.
 */
@Data
public class CustomerAddressBO {
    private Integer id;

    private Integer customerId;

    private Integer customerProvince;

    private Integer customerCity;
    private String cityName;

    private Integer customerDistrict;
    private String districtName;

    private String customerAddress;

}
