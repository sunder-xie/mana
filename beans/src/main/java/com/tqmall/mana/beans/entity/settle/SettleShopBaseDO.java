package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SettleShopBaseDO {
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

    //已结算的现金
    private BigDecimal settledCashAmount;

    //未结算的现金
    private BigDecimal payableCashAmount;

    //已结算的奖励金
    private BigDecimal settledBonusAmount;

    //未结算的奖励金
    private BigDecimal payableBonusAmount;

    //待生效的服务包数量
    private Integer waitEffectPackageNum;

    //待发货的服务包数量
    private Integer waitPackageNum;

    //配送中的服务包数量
    private Integer sendPackageNum;

    //已签收的服务包数量
    private Integer receivePackageNum;
}