package com.tqmall.mana.component.enums;

/**
 * Created by zhouheng on 16/12/28.
 */
public enum QuitStatusEnum {

    NO(0," 未退保"),
    YES(1,"已退保");

    private Integer code;

    private String desc;

    QuitStatusEnum(Integer code, String desc) {
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
        for(QuitStatusEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getDesc();
            }
        }
        return null;
    }
}
