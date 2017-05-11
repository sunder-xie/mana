package com.tqmall.mana.component.enums;

import lombok.Getter;

/**
 *
 * 结算数据操作 1：新增  2：修改
 *
 * Created by huangzhangting on 17/2/12.
 */
@Getter
public enum SettleDataOperateEnum {
    INSERT(1, "新增"),
    UPDATE(2, "修改");

    private Integer code;
    private String desc;

    SettleDataOperateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
