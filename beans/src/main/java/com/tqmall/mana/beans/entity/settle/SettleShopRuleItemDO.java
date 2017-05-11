package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.util.Date;

@Data
public class SettleShopRuleItemDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private Integer settleRuleId;

    private Integer settleCondition;

    private Integer rebateStandard;

    private Integer fundType;

    private Integer rebateType;

    private Integer calculateMode;

    private Date calculateTime;

    private Integer settleConfigBasicId;

    private Date startTime;

    private Date endTime;

}