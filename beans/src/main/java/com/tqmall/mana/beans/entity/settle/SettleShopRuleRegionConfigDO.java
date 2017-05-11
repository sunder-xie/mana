package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.util.Date;

@Data
public class SettleShopRuleRegionConfigDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private Integer settleRuleId;

    private String provinceCode;

    private String provinceName;

    private String cityCode;

    private String cityName;

}