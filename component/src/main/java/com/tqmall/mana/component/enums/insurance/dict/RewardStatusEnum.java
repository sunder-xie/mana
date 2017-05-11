package com.tqmall.mana.component.enums.insurance.dict;

/**
 *
 * 奖励金状态 1-未生效 2-已生效
 *
 * Created by huangzhangting on 17/2/8.
 */
public enum RewardStatusEnum {
    NOT_EFFECT(1, "未生效"),
    HAS_EFFECTED(2, "已生效");

    private Integer code;
    private String desc;

    RewardStatusEnum(Integer code, String desc) {
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
