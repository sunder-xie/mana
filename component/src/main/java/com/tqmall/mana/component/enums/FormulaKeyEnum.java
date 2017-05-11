package com.tqmall.mana.component.enums;

import lombok.Getter;

/**
 *
 * settle_fee_formula_config 表 formula_key 字段对应的枚举
 *
 * Created by huangzhangting on 17/3/10.
 */
@Getter
public enum FormulaKeyEnum {
    ALL_REBATE("all_rebate", "全部返利公式"),
    FORCE_INSURANCE("force_insurance", "交强险返利公式"),
    BIZ_INSURANCE("biz_insurance", "商业险返利公式");

    private String key;
    private String desc;

    FormulaKeyEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
