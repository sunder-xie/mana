package com.tqmall.mana.component.enums.insurance.dict;

import lombok.Getter;

/**
 *
 * 奖励金提现状态 1:未申请 2:申请中 3:审核通过 4:审核未通过 5:确认打款
 *
 * Created by huangzhangting on 17/2/15.
 */
@Getter
public enum WithdrawCashStatusEnum {
    NOT_APPLY(1, "未申请"),
    APPLYING(2, "申请中"),
    AUDIT_SUCCESS(3, "审核通过"),
    AUDIT_FAILED(4, "审核未通过"),
    CONFIRM_PAID(5, "确认打款");

    private Integer code;
    private String desc;

    WithdrawCashStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean isLegalCode(Integer code){
        if(code==null){
            return false;
        }
        for(WithdrawCashStatusEnum statusEnum : values()){
            if(statusEnum.getCode().equals(code)){
                return true;
            }
        }
        return false;
    }

    public static boolean canApplying(Integer code){
        return NOT_APPLY.getCode().equals(code)
                || AUDIT_FAILED.getCode().equals(code);
    }

    public static boolean canAudit(Integer code){
        return APPLYING.getCode().equals(code);
    }

    public static boolean canPay(Integer code){
        return AUDIT_SUCCESS.getCode().equals(code);
    }

}
