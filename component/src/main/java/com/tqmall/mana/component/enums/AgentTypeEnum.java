package com.tqmall.mana.component.enums;

/**
 * Created by zhouheng on 16/12/24.
 */
public enum AgentTypeEnum {

    SHOP(1, "门店"),
    PERSONAL(2, "个人");

    private Integer code;
    private String desc;

    AgentTypeEnum(Integer code, String desc) {
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
        for(AgentTypeEnum agentTypeEnum : values()){
            if(agentTypeEnum.getCode().equals(code)){
                return agentTypeEnum.getDesc();
            }
        }
        return null;
    }
}
