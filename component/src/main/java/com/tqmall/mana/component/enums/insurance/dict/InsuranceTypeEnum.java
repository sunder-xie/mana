package com.tqmall.mana.component.enums.insurance.dict;

/**
 * 保险类别枚举
 * Created by huangzhangting on 16/12/3.
 */
public enum InsuranceTypeEnum {
    FORCE_INSURANCE(1, "交强险"),
    BIZ_INSURANCE(2, "商业险");

    private Integer code;
    private String desc;

    InsuranceTypeEnum(Integer code, String desc) {
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
        for(InsuranceTypeEnum insuranceEnum : values()){
            if(insuranceEnum.getCode().equals(code)){
                return insuranceEnum.getDesc();
            }
        }
        return null;
    }
}
