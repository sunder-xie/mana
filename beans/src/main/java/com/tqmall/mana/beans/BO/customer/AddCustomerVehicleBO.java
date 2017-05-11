package com.tqmall.mana.beans.BO.customer;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by huangzhangting on 16/12/14.
 */
@Data
public class AddCustomerVehicleBO extends AddCommunicateRecordBO{

    private String licencePlate;

    private Integer hasLicencePlate;

    private String vehicleType;

    private Integer insureCompanyId;

    private Integer insureStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date insureEndDate;

    private String insureProvince;

    private String insureProvinceCode;

    private String insureCity;

    private String insureCityCode;

    private String insureVehicleType;

    private String engineNo;

    private String vinNo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date vehicleRegDate;

    private Integer insureIntention; //投保意向 1:意向强 2:考虑中 3:无意向 4:已投保

}
