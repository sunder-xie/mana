package com.tqmall.mana.component.enums;

/**
 * 优惠券状态
 * Created by huangzhangting on 16/12/7.
 */
public enum CouponStatusEnum {
    INIT_STATUS(0, "待发送"),
    SEND_SUCCESS(1, "发送成功"),
    SEND_FAILED(2, "发送失败"),
    HAS_EXPIRE(3, "已失效");

    private Integer code;
    private String desc;

    CouponStatusEnum(Integer code, String desc) {
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
        if(code==null){
            return null;
        }
        for(CouponStatusEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getDesc();
            }
        }
        return null;
    }
}
