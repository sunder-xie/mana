package com.tqmall.mana.beans.BO.settle.calculate;

import lombok.Data;

import java.util.List;

/**
 * Created by huangzhangting on 17/3/23.
 */
@Data
public class InsuranceSettleBasicBO {
    private Integer cooperationModeId; //和淘气合作模式
    private String insuranceOrderSn; //淘汽保单SN
//    private String applicantName; //投保人名称
//    private String beAppliedName; //被保人名称
    private String carOwnerName; //车主姓名
    private String vehicleSn; //车牌号码

    private Integer agentId; //代理人id
    private String agentName; //代理人名称
    private Integer insuranceCompanyId; //保险公司id

    private String insuredProvinceCode; //投保所在省CODE
    private String insuredCityCode; //投保所在城市CODE
    private String insuredProvince; //投保所在省
    private String insuredCity; //投保所在城市

    //保单信息
    List<InsuranceSettleFormBO> formBOList;
}
