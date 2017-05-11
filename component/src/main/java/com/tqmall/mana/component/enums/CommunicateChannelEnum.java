package com.tqmall.mana.component.enums;

/**
 * 沟通渠道
 * Created by huangzhangting on 16/12/16.
 */
public enum CommunicateChannelEnum {
    CUSTOMER_CALL(0, "车主致电"),
    CALL_BACK(1, "电话回访"),
    VISIT(2, "拜访");

    private Integer code;
    private String desc;

    CommunicateChannelEnum(Integer code, String desc) {
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
        for(CommunicateChannelEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getDesc();
            }
        }
        return null;
    }
}
