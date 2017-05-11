package com.tqmall.mana.component.enums;

/**
 * 是否
 * Created by zhouheng on 16/12/19.
 */
public enum YesNoEnum {

    NO(0,"否"),
    YES(1,"是");

    YesNoEnum(Integer code, String desc) {
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

    //根据code获取code解释
    public static String codeDescription(Integer code){
        if(code==null){
            return null;
        }
        for(YesNoEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getDesc();
            }
        }
        return null;
    }
}
