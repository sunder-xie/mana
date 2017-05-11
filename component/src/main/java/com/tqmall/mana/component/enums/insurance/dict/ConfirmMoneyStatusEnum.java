package com.tqmall.mana.component.enums.insurance.dict;

/**
 *
 * 淘汽确认收到保险公司款, 0:未收款 1:已收款
 *
 * Created by huangzhangting on 17/2/7.
 */
public enum ConfirmMoneyStatusEnum {
    NOT_CONFIRM(0, "未收款"),
    HAS_CONFIRMED(1, "已收款");

    private Integer code;
    private String desc;

    ConfirmMoneyStatusEnum(Integer code, String desc) {
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
