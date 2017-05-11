package com.tqmall.mana.beans.BO.customer;

import lombok.Data;

/**
 * Created by zhouheng on 16/12/18.
 */
@Data
public class SearchCustomerAddressBO {

    private Integer id;

    private Integer customerId;

    private Integer customerProvince;
    //前端用到
    private String customerProvinceName;

    private Integer customerCity;
    //前端用到
    private String customerCityName;

    private Integer customerDistrict;
    //前端用到
    private String customerDistrictName;

    private String customerAddress;
    //前端用到(组合)
    private String addressStr;


}
