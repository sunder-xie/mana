package com.tqmall.mana.beans.BO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zhouheng on 16/12/22.
 */
@Data
public class UcShopBO {

    /**
     * 门店id
     */
    Integer id;
    /**
     * 门店名称
     */
    String companyName;
    /**
     * 省份id
     */
    Integer provinceId;
    /**
     * 城市id
     */
    Integer cityId;
    /**
     * 区域id
     */
    Integer districtId;
    /**
     * 街道id
     */
    Integer streetId;
    /**
     * 详细地址
     */
    String address;
    /**
     * 邮编
     */
    String zipcode;
    /**
     * 联系人
     */
    String contactsName;
    /**
     * 手机号
     */
    String mobile;
    /**
     * 固定号码
     */
    String telephone;
    /**
     * 经度
     */
    BigDecimal longitude;
    /**
     * 纬度
     */
    BigDecimal latitude;
    /**
     * 高德经度
     */
    BigDecimal gdLongitude;
    /**
     * 高德纬度
     */
    BigDecimal gdLatitude;

    /** 是否有保险资格 0：否 1：有 */
    private Integer hasInsuranceRight = 0;

    /** 坐标是否有问题 0：否 1：是 */
    private Integer wrongPosition = 0;

}
