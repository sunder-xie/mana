package com.tqmall.mana.component.enums.insurance.dict;

/**
 *
 * 结算规则类型 1:现金 2:奖励金 3:服务包
 *
 * Created by huangzhangting on 17/1/13.
 */
public enum SettleRuleTypeEnum {
    CASH(1, "现金"),
    REWARD(2, "奖励金"),
    PACKAGE(3, "服务包");

    private Integer code;
    private String desc;

    SettleRuleTypeEnum(Integer code, String desc) {
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
        for(SettleRuleTypeEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getDesc();
            }
        }
        return null;
    }

}
