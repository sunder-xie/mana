package com.tqmall.mana.component.enums;

import lombok.Getter;

/**
 *
 * 公式变量枚举
 *
 * Created by huangzhangting on 17/3/11.
 */
@Getter
public enum FormulaVariableEnum {
    BIZ_INSURANCE_FEE("bizInsuranceFee", "商业险保费"),
    FORCE_INSURANCE_FEE("forceInsuranceFee", "交强险保费"),
    COUPON_AMOUNT("couponAmount", "现金券金额"),
    BIZ_INSURANCE_RATE("bizInsuranceRate", "商业险返利比例"),
    FORCE_INSURANCE_RATE("forceInsuranceRate", "交强险返利比例");

    private String key;
    private String desc;

    FormulaVariableEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
