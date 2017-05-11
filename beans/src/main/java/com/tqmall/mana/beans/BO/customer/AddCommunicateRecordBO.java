package com.tqmall.mana.beans.BO.customer;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by huangzhangting on 16/12/14.
 */
@Data
public class AddCommunicateRecordBO {

    private Integer id;

    private Integer customerId;

    private Integer customerVehicleId;

    private Integer communicateChannel;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date communicateDate;

    private String communicateContent;

    private Integer recommendShopId;

    private String recommendShopInfo;

    private String customerMobile;


    // 地址信息
    private Integer addressId;

    private Integer customerProvince;

    private Integer customerCity;

    private Integer customerDistrict;

    private String customerAddress;

}
