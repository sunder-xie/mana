package com.tqmall.mana.biz.mq.insurance;

import lombok.Data;

import java.util.List;

/**
 * Created by huangzhangting on 16/12/21.
 */
@Data
public class InsuranceBasicMsg {
    private Integer id;
    private Integer insuranceCompanyId;
    private Integer agentType;
    private Integer agentId;
    private String agentName;
    private String vehicleOwnerName;
    private String vehicleOwnerCertType;
    private String vehicleOwnerCertCode;
    private String vehicleOwnerPhone;
    /**车牌号码**/
    private String vehicleSn;
    /**是否取得车牌 1:有 0:没有**/
    private Integer hasLicense;
    /**配置型号**/
    private String carConfigType;
    /**发动机号**/
    private String carEngineSn;
    /**车架号**/
    private String carFrameSn;
    /**车辆登记日期**/
    private java.util.Date carEgisterDate;

    /**投保所在省CODE**/
    private String insuredProvinceCode;
    /**投保所在城市CODE**/
    private String insuredCityCode;
    /**投保所在省**/
    private String insuredProvince;
    /**投保所在城市**/
    private String insuredCity;

    /** 是否虚拟保单 0:否 1:是 */
    private Integer isVirtual = 0;

    List<InsuranceFormMsg> formMsgList;
}
