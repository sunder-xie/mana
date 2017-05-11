package com.tqmall.mana.component.enums;

/**
 * 门店来源
 * Created by huangzhangting on 16/12/16.
 */
public enum CustomerSourceEnum {
    TQ_MALL(0, "淘汽"),
    SHOP(1, "门店");

    private Integer code;
    private String desc;

    CustomerSourceEnum(Integer code, String desc) {
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
        for(CustomerSourceEnum sourceEnum : values()){
            if(sourceEnum.getCode().equals(code)){
                return sourceEnum.getDesc();
            }
        }
        return null;
    }
}
