package com.tqmall.mana.component.enums;

import lombok.Getter;

/**
 *
 * 比例类型 1:交强险比例 2:商业险比例
 *
 * Created by huangzhangting on 17/3/11.
 */
@Getter
public enum RateTypeEnum {
    FORCE_INSURANCE(1, "交强险比例"),
    BIZ_INSURANCE(2, "商业险比例");

    private Integer code;
    private String desc;

    RateTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
