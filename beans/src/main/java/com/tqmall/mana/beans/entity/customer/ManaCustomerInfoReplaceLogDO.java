package com.tqmall.mana.beans.entity.customer;

import lombok.Data;

import java.util.Date;

@Data
public class ManaCustomerInfoReplaceLogDO {
    private Integer id;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private String customerMobile;

    private String customerName;

    private Integer customerSource;

    private String certificateType;

    private String certificateNo;

    //车辆信息
    private Integer insureIntention;

    private String licencePlate;

    private Integer hasLicencePlate;

    private String vehicleType;

    private Integer insureCompanyId;

    private Integer insureStatus;

    private Date insureStartDate;

    private Date insureEndDate;

    private String insureProvince;

    private String insureProvinceCode;

    private String insureCity;

    private String insureCityCode;

    private String insureVehicleType;

    private String engineNo;

    private String vinNo;

    private Date vehicleRegDate;

}