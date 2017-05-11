package com.tqmall.mana.component.enums.insurance.dict;

import lombok.Getter;

/**
 *
 * 物料类型 1:机滤
 *
 * Created by huangzhangting on 17/2/21.
 */
@Getter
public enum MaterialTypeEnum {
    OIL_FILTER(1, "机滤");

    private Integer code;
    private String desc;

    MaterialTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
