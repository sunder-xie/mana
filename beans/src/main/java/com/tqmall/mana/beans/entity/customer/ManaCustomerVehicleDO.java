package com.tqmall.mana.beans.entity.customer;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;

@Data
public class ManaCustomerVehicleDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private Integer customerId;

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

    private String remarkInfo;

    private Integer insureIntention; //投保意向 1:意向强 2:考虑中 3:无意向 4:已投保

    private Integer hasSync; //是否跟insurance同步过数据 0:否 1:是

    private Integer cooperationMode; //和淘气合作模式 1:奖励金 2:买保险送服务 3:买服务送保险

    private Integer agentType; //代理人类型 1:门店 2:个人

    private Integer agentId; //卖保险代理人id

    private String agentName; //卖保险代理人名称

    private Integer insuranceFormId; //最新的保单id

    private Integer quitInsureStatus; //在淘汽投保状态 0:未投保 1:已投保 2:已退保


    public boolean isEmpty() {
        if (!StringUtils.isEmpty(licencePlate)) {
            return false;
        }
        if (hasLicencePlate != null) {
            return false;
        }
        if (!StringUtils.isEmpty(vehicleType)) {
            return false;
        }
        if (insureCompanyId != null && insureCompanyId > 1) {
            return false;
        }
        if (insureStatus != null && insureStatus >= 0) {
            return false;
        }
        if (insureEndDate != null) {
            return false;
        }
        if (!StringUtils.isEmpty(insureProvinceCode)) {
            return false;
        }
        if (!StringUtils.isEmpty(insureCityCode)) {
            return false;
        }
        if (!StringUtils.isEmpty(insureVehicleType)) {
            return false;
        }
        if (!StringUtils.isEmpty(engineNo)) {
            return false;
        }
        if (!StringUtils.isEmpty(vinNo)) {
            return false;
        }
        if (vehicleRegDate != null) {
            return false;
        }
        if (insureIntention != null && insureIntention > 0) {
            return false;
        }

        return true;
    }
}