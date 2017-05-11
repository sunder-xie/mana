package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SettleCompanyCheckDetailDO {
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

    //settle_form 的唯一主键,基础信息的获得
    private String settleFormSn;

    //保险公司id
    private Integer insuranceCompanyId;

    //保险公司名称
    private String insuranceCompanyName;

    //购买保险场景id 1:商业险 2:商业交强同保 3:交强险 4:第三方责任交强同保 5:责任险单保
    private Integer scenarioTypeId;

    //保费分成比例
    private BigDecimal insuredRoyaltyRate;

    //保费分成，保险公司给淘汽
    private BigDecimal insuredRoyaltyFee;

    //淘汽确认收到保险公司款, 0:未收款 1:已收款
    private Integer confirmMoneyStatus;

    //确认收款时间
    private Date confirmMoneyTime;

    //收款人员
    private String confirmMoneyPeopleName;
}