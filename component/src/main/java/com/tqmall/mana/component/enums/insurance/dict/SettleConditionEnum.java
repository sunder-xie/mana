package com.tqmall.mana.component.enums.insurance.dict;

/**
 *
 * 结算条件 1:签单日期 2:起保日期 3:服务包支付日期
 * 签单日期：保单缴费成功时间
 *
 * Created by huangzhangting on 17/1/13.
 */
public enum SettleConditionEnum {
    SIGN_DATE(1, "签单日期"),
    INSURED_DATE(2, "起保日期"),
    PACKAGE_PAY_DATE(3, "服务包支付日期"),
    ALLOWANCE_EFFECT(4, "补贴生效时间"),
    COUPON_CASH_TIME(5, "现金券兑现时间");

    private Integer code;
    private String desc;

    SettleConditionEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String codeDescription(Integer code){
        if(code == null){
            return null;
        }
        for(SettleConditionEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getDesc();
            }
        }
        return null;
    }

}
