package com.tqmall.mana.component.enums;

/**
 * 在淘汽投保状态
 * Created by huangzhangting on 16/12/22.
 */
public enum QuitInsureStatusEnum {
    NOT_INSURE(0, "未投保"),
    HAS_INSURED(1, "已投保"),
    QUIT_INSURED(2, "已退保");

    private Integer code;
    private String desc;

    QuitInsureStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getCode() {
        return code;
    }

    //根据code获取code解释
    public static String codeDescription(Integer code){
        if(code==null){
            return null;
        }
        for(QuitInsureStatusEnum statusEnum : values()){
            if(statusEnum.getCode().equals(code)){
                return statusEnum.getDesc();
            }
        }
        return null;
    }

}
