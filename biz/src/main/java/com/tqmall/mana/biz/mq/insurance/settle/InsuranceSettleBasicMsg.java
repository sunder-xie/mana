package com.tqmall.mana.biz.mq.insurance.settle;

import lombok.Data;

import java.util.List;

/**
 *
 * 对应 insurance_basic 的信息
 *
 * Created by huangzhangting on 17/1/20.
 */
@Data
public class InsuranceSettleBasicMsg {
    private Integer cooperationModeId; //和淘气合作模式
    private String insuranceOrderSn; //淘汽保单SN
    private String applicantName; //投保人名称
    private String beAppliedName; //被保人名称
    private String carOwnerName; //车主姓名
    private String vehicleSn; //车牌号码

    private Integer agentId; //代理人id
    private String agentName; //代理人名称
    private Integer insuranceCompanyId; //保险公司id

    private String insuredProvinceCode; //投保所在省CODE
    private String insuredCityCode; //投保所在城市CODE
    private String insuredProvince; //投保所在省
    private String insuredCity; //投保所在城市

    //真实保单信息
    List<InsuranceSettleFormMsg> formMsgList;

    //服务包信息（虚拟保单）
    InsuranceSettlePackageMsg packageMsg;

    private String agentAccount; //门店账号（mana内部代码逻辑使用，消息发送方无需关注）
    private Integer agentTag; //门店标签（mana内部代码逻辑使用，消息发送方无需关注）

}
