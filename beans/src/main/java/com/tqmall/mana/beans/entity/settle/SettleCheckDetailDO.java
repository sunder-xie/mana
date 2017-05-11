package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SettleCheckDetailDO {
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

    //门店id
    private Integer agentId;

    //该记录所在表的唯一主键id，通过此+cooperation_mode_id+settle_project_id 确定唯一一条记录
    private Integer settleSqlTableId;

    //insurance_basic 的唯一主键id
    private Integer insuranceBasicId;

    //模式，例如模式一：买保险送服务包
    private Integer cooperationModeId;

    //模式名称
    private String cooperationModeName;

    //对账类型：1:现金 2:奖励金 3:服务包,来自mana字典表
    private Integer settleRuleType;

    //车牌号
    private String licenseNo;

    //车主姓名
    private String carOwnerName;

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

    //审核状态, 0-未审核 1-已审核
    private Integer auditStatus;

    //审核人员
    private String auditPeopleName;

    //审核时间
    private Date auditTime;

    //结算比例
    private BigDecimal settleRate;

    //结算金额
    private BigDecimal settleFee;

    //结算状态, 0-未结算 1-已结算
    private Integer settleFeeStatus;

    //结算人员
    private String settlePeopleName;

    //结算时间
    private Date settleTime;

    //服务包名称
    private String settlePackageName;

    //服务包状态, 0--待发货, 2-配送中 3-已签收
    private Integer settlePackageStatus;

}