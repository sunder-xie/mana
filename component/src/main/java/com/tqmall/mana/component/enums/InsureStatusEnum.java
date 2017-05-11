package com.tqmall.mana.component.enums;

/**
 * 车险状态
 * Created by huangzhangting on 16/12/16.
 */
public enum InsureStatusEnum {
    NOT_INSURE(0, "未投保"),
    HAS_INSURED(1, "在保"),
    OFF_INSURED(2, "脱保");

    private Integer code;
    private String desc;

    InsureStatusEnum(Integer code, String desc) {
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
        for(InsureStatusEnum statusEnum : values()){
            if(statusEnum.getCode().equals(code)){
                return statusEnum.getDesc();
            }
        }
        return null;
    }
}
