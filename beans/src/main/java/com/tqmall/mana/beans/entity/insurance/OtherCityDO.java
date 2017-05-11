package com.tqmall.mana.beans.entity.insurance;

import lombok.Data;

import java.util.Date;

@Data
public class OtherCityDO {
    public static final String PING_AN_SOURCE = "ping_an";
    public static final String REN_BAO_SOURCE = "ren_bao";

    private Integer id;

    private String isDeleted;

    private Date gmtModified;

    private Date gmtCreate;

    private String provinceName;

    private String provinceCode;

    private String cityName;

    private String cityAllName;

    private String cityCode;

    private String sourceName;

}