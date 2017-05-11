package com.tqmall.mana.component.enums.insurance.dict;

/**
 *
 * 结算状态, 0-未审核 1-未支付 2-已支付
 *
 * Created by huangzhangting on 17/1/21.
 */
public enum SettleStatusEnum {
    NOT_AUDIT(0, "未审核"),
    NOT_PAY(1, "未支付"),
    HAS_PAID(2, "已支付");

    private Integer code;
    private String desc;

    SettleStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String codeDescription(Integer code){
        if(code == null){
            return null;
        }
        for(SettleStatusEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getDesc();
            }
        }
        return null;
    }
}
