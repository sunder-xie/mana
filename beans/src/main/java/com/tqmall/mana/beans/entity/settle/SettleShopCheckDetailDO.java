package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SettleShopCheckDetailDO {
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

    //各结算项目的业务唯一主键,基础信息的获得,加settle_project_id
    private String bizSn;

    //真实保单唯一root，冗余自insurance_basic.insurance_order_sn
    private String insuranceOrderSn;

    //门店id，冗余自insurance_basic
    private Integer agentId;

    //对账类型：1:现金 2:奖励金 3:服务包,来自mana字典表
    private Integer settleRuleType;

    //模式，例如模式一：买保险送服务包
    private Integer cooperationModeId;

    //模式名称
    private String cooperationModeName;

    //车主,冗余自insurance_basic.insured_name
    private String carOwnerName;

    //车牌号，冗余自insurance_virtual_basic.vehicle_sn
    private String vehicleSn;

    //保险公司id
    private Integer insuranceCompanyId;

    //保险公司名称
    private String insuranceCompanyName;

    //结算项目id
    private Integer settleProjectId;

    //结算项目名称,例如-商业险返利
    private String settleProjectName;

    //结算条件id
    private Integer settleConditionId;

    //结算条件名称，例如-起保时间
    private String settleConditionName;

    //结算条件值
    private Date settleConditionTime;

    //结算比例,若是工时费则比例为0
    private BigDecimal settleRate;

    //结算金额
    private BigDecimal settleFee;

    //结算状态 1:未结算 2:已结算
    private Integer settleFeeStatus;

    //审核人员
    private String auditPeopleName;

    //审核时间
    private Date auditTime;

    //结算人员
    private String settlePeopleName;

    //结算时间
    private Date settleTime;

    //erp门店对账数据查询只查标识为1的数据
    private Integer erpFlag;

    //门店名称，冗余自insurance_basic
    private String agentName;

    //门店帐号，根据agent_id获得uc的帐号
    private String agentAccount;

    //门店标签 1:云修店 2:档口店
    private Integer agentTag;

    //新车牌号
    private String newVehicleSn;

    //是否用券 0:否 1:是
    private Integer ifUseCoupon;
    //用券数量
    private Integer couponNumber;
    //券金额
    private BigDecimal couponAmount;
    //结算基数
    private BigDecimal settleBaseAmount;

    //对账状态 1:未申请 2:申请中 3:审核通过 4:审核未通过 5:确认打款
    private Integer balanceStatus;
    //申请人
    private String applyPeopleName;
    //申请时间
    private Date applyTime;

}