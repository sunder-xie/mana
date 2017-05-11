package com.tqmall.mana.component.enums;

/**
 * 投保意向
 * Created by huangzhangting on 16/12/16.
 */
public enum InsureIntentionEnum {
    STRONG(1, "意向强"),
    THINKING(2, "考虑中"),
    NO_INTENTION(3, "无意向"),
    HAS_INSURED(4, "已投保");

    private Integer code;
    private String desc;

    InsureIntentionEnum(Integer code, String desc) {
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
        for(InsureIntentionEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getDesc();
            }
        }
        return null;
    }
}
