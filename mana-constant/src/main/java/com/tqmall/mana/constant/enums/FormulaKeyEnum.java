package com.tqmall.mana.constant.enums;

import lombok.Getter;

/**
 *
 * settle_fee_formula_config 表 formula_key 字段对应的枚举
 *
 */
@Getter
public enum FormulaKeyEnum {
    FORCE_INSURANCE("force_insurance", "交强险返利公式"),
    BIZ_INSURANCE("biz_insurance", "商业险返利公式");

    private String key;
    private String desc;

    FormulaKeyEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
