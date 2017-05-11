package com.tqmall.mana.beans.entity.customer;

import lombok.Data;

import java.util.Date;

/**
 * 首页客户来电查询分页
 * Created by zhouheng on 16/12/26.
 */
@Data
public class CustomerVehiclePageDO {

    private Integer customerId;

    private Integer vehicleId;

    private String customerName;

    private String customerMobile;

    private String licencePlate;

    private Integer customerSource;

    private Integer insureStatus;

    private Date insureEndDate;

    private Integer insureIntention;


}
