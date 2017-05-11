package com.tqmall.mana.test;

/**
 * Created by huangzhangting on 17/3/29.
 */
public enum EnumTest {
    GREEN(1),
    RED(2),
    BLUE(3);

    private Integer code;

    EnumTest(Integer code) {
        this.code = code;
    }
}
