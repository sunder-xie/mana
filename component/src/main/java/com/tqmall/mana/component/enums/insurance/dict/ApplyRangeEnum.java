package com.tqmall.mana.component.enums.insurance.dict;

/**
 *
 * 适用范围 0:保险公司通用 1:地区 2:门店
 *
 * Created by huangzhangting on 17/1/13.
 */
public enum ApplyRangeEnum {
    INSURANCE_COMPANY(0, "保险公司"),
    REGION(1, "地区"),
    SHOP(2, "门店");

    private Integer code;
    private String desc;

    ApplyRangeEnum(Integer code, String desc) {
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
        for(ApplyRangeEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getDesc();
            }
        }
        return null;
    }

}
