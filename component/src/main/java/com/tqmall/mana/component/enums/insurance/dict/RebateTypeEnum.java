package com.tqmall.mana.component.enums.insurance.dict;

/**
 *
 * 返点类型 1:比例 2:服务包工时费
 *
 * Created by huangzhangting on 17/1/13.
 */
public enum RebateTypeEnum {
    RATE(1, "比例"),
    SERVICE_FEE(2, "服务包工时费");

    private Integer code;
    private String desc;

    RebateTypeEnum(Integer code, String desc) {
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
        for(RebateTypeEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getDesc();
            }
        }
        return null;
    }

}
