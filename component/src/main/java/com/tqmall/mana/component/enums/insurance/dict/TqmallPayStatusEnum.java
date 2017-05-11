package com.tqmall.mana.component.enums.insurance.dict;

import lombok.Getter;

/**
 * 淘汽支付到保险公司状态, 0-未审核 1-未支付 2-已支付
 */
@Getter
public enum TqmallPayStatusEnum {
    NOT_AUDIT(0, "未审核"),
    NOT_PAY(1, "未支付"),
    HAS_PAID(2, "已支付");

    private Integer code;
    private String desc;

    TqmallPayStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
