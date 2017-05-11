package com.tqmall.mana.component.enums.insurance.dict;

import lombok.Getter;

/**
 *
 * 结算金额修改标志 0:未修改 1:已修改
 *
 * Created by huangzhangting on 17/3/8.
 */
@Getter
public enum SettleFeeModifyFlagEnum {
    NOT_MODIFY(0, "未修改"),
    HAS_MODIFIED(1, "已修改");

    private Integer code;
    private String desc;

    SettleFeeModifyFlagEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
