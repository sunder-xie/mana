package com.tqmall.mana.biz.mq.insurance.settle;

/**
 *
 * 消息类型
 * 1:保单基础信息 即 msgContent 对应的是 InsuranceSettleBasicMsg
 * 2:物料补贴信息 即 msgContent 对应的是 InsuranceSettleMaterialAllowanceMsg
 *
 * Created by huangzhangting on 17/2/8.
 */
public enum SettleMsgTypeEnum {
    FORM_BASIC(1, "保单基础信息"),
    MATERIAL_ALLOWANCE(2, "物料补贴信息");

    private Integer code;
    private String desc;

    SettleMsgTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
