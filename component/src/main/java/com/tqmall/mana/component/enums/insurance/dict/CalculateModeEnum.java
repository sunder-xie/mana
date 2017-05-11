package com.tqmall.mana.component.enums.insurance.dict;

/**
 *
 * 计算方式 1:单笔 2:月累计
 *
 * Created by huangzhangting on 17/1/12.
 */
public enum CalculateModeEnum {
    SINGLE(1, "单笔");
//    MONTHLY_CUMULATIVE(2, "月累计");

    CalculateModeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;
    private String desc;

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
        for(CalculateModeEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getDesc();
            }
        }
        return null;
    }

}
