package com.tqmall.mana.component.enums.insurance.dict;

/**
 *
 * 返点基准 1:交强险 2:商业险 3:服务包
 *
 * Created by huangzhangting on 17/1/13.
 */
public enum RebateStandardEnum {
    FORCE_INSURANCE(1, "交强险"),
    BIZ_INSURANCE(2, "商业险"),
    PACKAGE(3, "服务包");

    private Integer code;
    private String desc;

    RebateStandardEnum(Integer code, String desc) {
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
        for(RebateStandardEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getDesc();
            }
        }
        return null;
    }

}
