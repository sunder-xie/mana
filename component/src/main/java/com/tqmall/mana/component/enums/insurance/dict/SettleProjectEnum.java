package com.tqmall.mana.component.enums.insurance.dict;

/**
 *
 * 结算项目 1:商业险返利 2:交强险返利 3:服务包返利 4:机滤补贴 5:优惠劵补贴 6:服务包物料 7:现金券兑现
 *
 * Created by huangzhangting on 17/1/13.
 */
public enum SettleProjectEnum {
    BIZ_REBATE(1, "商业险返利"),
    FORCE_REBATE(2, "交强险返利"),
    PACKAGE_REBATE(3, "服务包返利"),
    OIL_FILTER_REBATE(4, "机滤补贴"),
    COUPON_REBATE(5, "优惠劵补贴"),
    PACKAGE_GOODS(6, "服务包物料"),
    CASH_COUPON(7, "现金券兑现");

    private Integer code;
    private String desc;

    SettleProjectEnum(Integer code, String desc) {
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
        for(SettleProjectEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getDesc();
            }
        }
        return null;
    }

}
