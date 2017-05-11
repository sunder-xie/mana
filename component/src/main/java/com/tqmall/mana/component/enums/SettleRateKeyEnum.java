package com.tqmall.mana.component.enums;

import lombok.Getter;

/**
 *
 * 比例配置key枚举
 *
 * settle_rate_config 表 rate_key 对应的枚举
 *
 * Created by huangzhangting on 17/3/10.
 */
@Getter
public enum SettleRateKeyEnum {
    BIZ_INSURANCE("biz_insurance", "商业险比例"),
    FORCE_INSURANCE("force_insurance", "交强险比例");

    private String key;
    private String desc;

    SettleRateKeyEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
