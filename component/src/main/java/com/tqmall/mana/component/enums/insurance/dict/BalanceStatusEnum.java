package com.tqmall.mana.component.enums.insurance.dict;

/**
 *
 * 对账状态 1:未申请 2:申请中 3:审核通过 4:审核未通过 5:确认打款
 *
 * Created by huangzhangting on 17/3/14.
 */
public enum BalanceStatusEnum {
    NOT_APPLY(1, "未申请"),
    APPLYING(2, "申请中"),
    AUDIT_SUCCESS(3, "审核通过"),
    AUDIT_FAILED(4, "审核未通过"),
    CONFIRM_PAID(5, "确认打款");

    private Integer code;
    private String desc;

    BalanceStatusEnum(Integer code, String desc) {
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
        for(BalanceStatusEnum statusEnum : values()){
            if(code.equals(statusEnum.getCode())){
                return statusEnum.getDesc();
            }
        }
        return null;
    }

    public static boolean isLegalCode(Integer code){
        if(code==null){
            return false;
        }
        for(BalanceStatusEnum statusEnum : values()){
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
