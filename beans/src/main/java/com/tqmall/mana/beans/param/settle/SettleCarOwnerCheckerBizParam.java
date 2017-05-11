package com.tqmall.mana.beans.param.settle;

import lombok.Data;

import java.util.Date;

/**
 * Created by zengjinju on 17/1/21.
 */
@Data
public class SettleCarOwnerCheckerBizParam {
    private Integer id;
    private Integer page = 1;
    private Integer pageSize = 20;
    //车主姓名
    private String vehiclePeopleName;
    //车牌号
    private String vehicleSn;
    //淘气内部订单号
    private String insuranceOrderSn;
    //二期支付流水号
    private String secondPayNo;
    //一期支付流水号
    private String firstPayNo;
    //二期支付状态(0-未支付 1- 已支付)
    private Integer secondPayStatus;
    //一期支付起始日期
    private Date gmtFirstPaidStart;
    //一期支付结束日期
    private Date gmtFirstPaidEnd;
    //二期支付起始日期
    private Date gmtSecondPaidStart;
    //二期支付结束日期
    private Date gmtSecondPaidEnd;
    //门店名称
    private String agentName;
    //保费支付状态
    private Integer isTqmallPayStatus;
    //淘气收款确认状态
    private Integer isTqmallGetPaid;
    //订单编号
    private String settleServiceSn;
    //保险公司id
    private Integer insuranceCompanyId;
    //门店标签 1:云修店 2:档口店
    private Integer agentTag;
}
