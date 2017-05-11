package com.tqmall.mana.component.enums.insurance.dict;

/**
 * 投保场景 1:商业险 2:商业交强同保 3:交强险 4:第三方责任交强同保 5:责任险单保
 * <p>
 * Created by huangzhangting on 17/1/13.
 */
public enum ScenarioTypeEnum {
    SYX(1, "单保商业险"),
    SYJQTB(2, "商业险+交强险"),
    JQX(3, "单保交强险"),
    ZRXJQXTB(4, "第三方责任交强同保"),
    ZRXDB(5, "单保第三方责任险"),
    ZRXCSRYZRXTB(6,"第三方责任+车上人员责任险（司机或乘客）"),
    ZRXCSRYZRXJQXTB(7,"第三方责任+车上人员责任险（司机或乘客）+交强险");

    private Integer code;
    private String desc;

    ScenarioTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String codeDescription(Integer code) {
        if (code == null) {
            return null;
        }
        for (ScenarioTypeEnum enums : values()) {
            if (enums.getCode().equals(code)) {
                return enums.getDesc();
            }
        }
        return null;
    }

}
