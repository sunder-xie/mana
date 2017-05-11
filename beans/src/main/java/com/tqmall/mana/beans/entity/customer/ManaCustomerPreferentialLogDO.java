package com.tqmall.mana.beans.entity.customer;

import lombok.Data;

import java.util.Date;

@Data
public class ManaCustomerPreferentialLogDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private Integer customerVehicleId;

    private String customerMobile;

    private Date sendDate;

    private String preferentialContent;

    private Integer preferentialType;

    private String preferentialTypeName;

    private Integer preferentialNum;

}