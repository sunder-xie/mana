package com.tqmall.mana.beans.entity.customer;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;

@Data
public class ManaCustomerAddressDO {

    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private Integer customerId;

    private Integer customerProvince;

    private Integer customerCity;

    private Integer customerDistrict;

    private String customerAddress;


    public boolean isEmpty() {
        if (customerProvince != null) {
            return false;
        }
        if (customerCity != null) {
            return false;
        }
        if (customerDistrict != null) {
            return false;
        }

        return StringUtils.isEmpty(customerAddress);
    }

    /* 设置默认值（修改地址时用到） */
    public void setDefaultProperty(){
        if(customerProvince==null){
            customerProvince = 0;
        }
        if(customerCity==null){
            customerCity = 0;
        }
        if(customerDistrict==null){
            customerDistrict = 0;
        }
    }

}