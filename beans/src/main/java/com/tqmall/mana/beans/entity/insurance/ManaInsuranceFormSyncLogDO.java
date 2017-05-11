package com.tqmall.mana.beans.entity.insurance;

import lombok.Data;

import java.util.Date;

@Data
public class ManaInsuranceFormSyncLogDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private Integer customerVehicleId;

    private Integer insuranceFormId;

    private Integer insuranceBasicId;

    private Integer insureCompanyId;

    private Integer cooperationMode;

    private String vehicleOwnerCertType;

    private String vehicleOwnerCertCode;

    private String vehicleOwnerName;

    private String vehicleOwnerPhone;

    private Integer agentType;

    private Integer agentId;

    private String agentName;

    private Date insureStartTime;

    private Date insureEndTime;

    private String insuredProvince;

    private String insuredCity;

    private String insuredProvinceCode;

    private String insuredCityCode;

    private String licencePlate;

    private Integer hasLicencePlate;

    private String configType;

    private String engineNo;

    private String vinNo;

    private Date vehicleRegDate;

    private Integer isVirtual; //是否虚拟保单 0:否 1:是

    private Integer virtualInsuranceFormId; //虚拟保单id 关联insurance_virtual_form.id
}