package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.util.Date;

@Data
public class SettleScenarioMatchRegularDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private Integer insuranceCompanyId;

    private String insuranceCompanyName;

    private Integer scenarioType;

    private String matchRegular;

}