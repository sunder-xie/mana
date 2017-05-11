package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.util.Date;

@Data
public class SettleRateRegionConfigDO {
    //主键ID
    private Integer id;

    //是否删除,Y删除,N未删除
    private String isDeleted;

    //创建时间
    private Date gmtCreate;

    //更新时间
    private Date gmtModified;

    //创建人
    private String creator;

    //修改人
    private String modifier;

    //settle_rate_config表主键id
    private Integer settleRateConfigId;

    //省份编码
    private String provinceCode;

    //省份名称
    private String provinceName;

    //城市编码
    private String cityCode;

    //城市名称
    private String cityName;

}