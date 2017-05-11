package com.tqmall.mana.component.enums.insurance.dict;

/**
 * Created by zhouheng on 16/12/25.
 */
public enum CooperationModeEnum {

    GIVE_REWARD(1,"买保险送奖励金"),
    GIVE_PACKAGE(2,"买保险送服务包"),
    GIVE_INSURANCE(3,"买服务包送保险");

    private Integer code;
    private String desc;

    CooperationModeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    //根据code获取code解释
    public static String codeDescription(Integer code){
        if(code==null){
            return null;
        }
        for(CooperationModeEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getDesc();
            }
        }
        return null;
    }

}
