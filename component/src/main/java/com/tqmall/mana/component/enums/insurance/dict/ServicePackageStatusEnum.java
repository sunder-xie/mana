package com.tqmall.mana.component.enums.insurance.dict;

/**
 *
 * 服务包状态, 0--待发货, 1-配送中 2-已签收
 *
 * Created by huangzhangting on 17/1/22.
 */
public enum ServicePackageStatusEnum {
    WAIT_SEND(0, "待发货"),
    DISPATCHING(1, "配送中"),
    HAS_SIGNED(2, "已签收"),
    NOT_EFFECT(3, "待生效");

    private Integer code;
    private String desc;

    ServicePackageStatusEnum(Integer code, String desc) {
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
