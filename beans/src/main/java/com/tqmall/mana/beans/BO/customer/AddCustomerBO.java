package com.tqmall.mana.beans.BO.customer;

import lombok.Data;

/**
 * Created by huangzhangting on 16/12/14.
 */
@Data
public class AddCustomerBO extends AddCustomerVehicleBO{

    private String customerName;

    private Integer customerSource;

    private String certificateType;

    private String certificateNo;


}
