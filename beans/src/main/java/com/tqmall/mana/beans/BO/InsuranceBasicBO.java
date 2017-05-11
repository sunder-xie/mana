package com.tqmall.mana.beans.BO;

import lombok.Data;

/**
 * 保险基础信息
 * <p>
 * Created by huangzhangting on 16/12/3.
 */
@Data
public class InsuranceBasicBO {
    /**
     * 数据库主键id
     */
    private Integer bid;

    /**
     * 代理人类型, 1:门店 2:个人
     **/
    private Integer agentType;

    /**
     * 卖保险代理人id
     **/
    private Integer agentId;

    /**
     * 卖保险代理人名称
     **/
    private String agentName;

    /**
     * 投保所在省
     */
    private String insuredProvince;

    /**
     * 投保所在城市
     */
    private String insuredCity;

    /**
     * 车牌号码
     */
    private String vehicleSn;

    /**
     * 车型编号
     */
    private String vehicleCode;

    /**
     * 配置型号
     */
    private String carConfigType;

    /**
     * 发动机号
     */
    private String carEngineSn;

    /**
     * 车架号
     */
    private String carFrameSn;
    /**
     * 车主名称
     */
    private String vehicleOwnerName;
    /**
     * 车主证件名称
     */
    private String vehicleOwnerCertTypeName;
    /**
     * 车主证件编码
     */
    private String vehicleOwnerCertCode;
    /**
     * 车主手机号
     */
    private String vehicleOwnerPhone;

    /**
     * 投保人名称
     */
    private String applicantName;
    /**
     * 投保人证件名称
     */
    private String applicantCertTypeName;
    /**
     * 投保人证件编码
     */
    private String applicantCertCode;
    /**
     * 投保人手机号
     */
    private String applicantPhone;

    /**
     * 被保人名称
     */
    private String insuredName;
    /**
     * 被保人证件名称
     */
    private String insuredCertTypeName;
    /**
     * 被保人证件编码
     */
    private String insuredCertCode;
    /**
     * 被保人手机号
     */
    private String insuredPhone;

}
