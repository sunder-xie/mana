package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.util.Date;

@Data
public class SettleConfigBasicDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private String groupName;

    private Integer insuranceCompanyId;

    private String insuranceCompanyName;

    private Integer cooperationMode;

    private Integer insuranceType;

    private Integer calculateMode;

}