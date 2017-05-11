package com.tqmall.mana.component.enums;

/**
 * Created by zhouheng on 16/12/27.
 */
public enum FormStatusEnum {

    TEMP_FORM(0,"暂存"),
    HAS_SUBMIT(1,"已提交"),
    WAIT_FEE(2,"待缴费"),
    HAS_FEE(3,"已缴费");

    private Integer code;
    private String desc;

    FormStatusEnum(Integer code, String desc) {
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
        for(FormStatusEnum formEnum : values()){
            if(formEnum.getCode().equals(code)){
                return formEnum.getDesc();
            }
        }
        return null;
    }

}
