package com.tqmall.mana.component.enums.insurance.dict;

/**
 * Created by zwb on 17/2/20.
 */
public enum AllowanceStatusEnum {
    WSQ(1,"未申请"),
    YSQ(2,"已申请"),
    YSH(3,"已审核"),
    YFK(4,"已付款");
    private Integer code;
    private String desc;
    AllowanceStatusEnum(Integer code,String desc){
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
        for(AllowanceStatusEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getDesc();
            }
        }
        return null;
    }
}

