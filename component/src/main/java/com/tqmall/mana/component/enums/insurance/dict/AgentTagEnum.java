package com.tqmall.mana.component.enums.insurance.dict;

import lombok.Getter;

/**
 *
 * 门店标签 1:云修店 2:档口店
 *
 * Created by huangzhangting on 17/4/12.
 */
@Getter
public enum AgentTagEnum {
    YUN_XIU(1, "云修店"),
    DANG_KOU(2, "档口店");

    private Integer code;
    private String desc;

    AgentTagEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String codeDescription(Integer code){
        if(code==null){
            return null;
        }
        for(AgentTagEnum tagEnum : values()){
            if(tagEnum.getCode().equals(code)){
                return tagEnum.getDesc();
            }
        }
        return null;
    }
}
