package com.tqmall.mana.beans.entity.customer;

import lombok.Data;

import java.util.Date;

@Data
public class ManaCustomerDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private String customerMobile;

    private String customerName;

    private Integer customerSource;

    private String certificateType;

    private String certificateNo;

    private Integer relationId;

    private String remarkInfo;

    private Integer hasSync; //是否跟insurance同步过数据 0:否 1:是

}