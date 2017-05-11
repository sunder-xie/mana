package com.tqmall.mana.beans.BO.customer;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by zhouheng on 16/12/22.
 */
@Data
public class UpdateCustomerInfoBO {

    /**
     * 客户ID
     */
    private Integer id;

    private Integer vehicleId;

    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户手机号
     */
    private String customerMobile;
    /**
     * 车牌号
     */
    private String licencePlate;
    /**
     * 客户来源编码
     */
    private Integer customerSource;

    /**
     * 品牌类型
     */
    private String vehicleType;

    /**
     * 车险到期日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date insureEndDate;

    /**
     * 保险状态
     */
    private Integer insureStatus;

    /**
     * 保险意向
     */
    private Integer insureIntention;
    /**
     * 投保省份名称
     */
    private String insureProvince;
    /**
     * 投保省份编码
     */
    private String insureProvinceCode;
    /**
     * 保险公司id
     */
    private Integer insureCompanyId;
    /**
     * 投保城市名称
     */
    private String insureCity;
    /**
     * 投保城市编码
     */
    private String insureCityCode;
    /**
     * 证件类型
     */
    private String certificateType;
    /**
     * 证件类型编码
     */
    private String certificateNo;
    /**
     * 保险车来那个品牌型号
     */
    private String insureVehicleType;
    /**
     * 发动机号
     */
    private String engineNo;
    /**
     * 车架号
     */
    private String vinNo;
    /**
     * 车辆登记日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date vehicleRegDate;

    /**
     * 地址信息
     */
    private Integer addressId;
    private Integer customerProvince;

    private Integer customerCity;

    private Integer customerDistrict;

    private String customerAddress;

}
