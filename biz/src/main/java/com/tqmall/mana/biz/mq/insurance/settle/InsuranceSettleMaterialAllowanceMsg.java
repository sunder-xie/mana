package com.tqmall.mana.biz.mq.insurance.settle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * 物料补贴消息对象
 *
 * Created by huangzhangting on 17/2/8.
 */
@Data
public class InsuranceSettleMaterialAllowanceMsg {
    private Integer cooperationModeId; //和淘气合作模式
    private String insuranceOrderSn; //淘汽保单SN
    private String materialOrderSn;//物料订单号SN
    private String beAppliedName; //被保人名称
    private String carOwnerName; //车主姓名
    private String vehicleSn; //车牌号码
    private Integer agentId; //代理人id
    private String agentName; //代理人名称
    private Integer insuranceCompanyId; //保险公司id

    private Integer materialType; //物料类型 1:机滤
    private BigDecimal payableAmount; //应补贴金额
    private Date allowanceEffectTime; //补贴生效时间

    private Integer whsId;//订单仓库Id
    private String whsName;//订单仓库名字
    private Integer materialNum;//物料数量

}
