package com.tqmall.mana.external.dubbo.stall;

import com.tqmall.core.common.entity.Result;

import java.util.Map;

/**
 * Created by huangzhangting on 16/7/12.
 */
public interface ExtSmsService {
    /**
     * 发送短信
     * @param mobiles 手机号码，多个用,隔开
     * @param action 模块key，短信模板在数据库表中的唯一标识
     * @param dataMap 短信模板中待替换的字符串-值map，例如：{code: 1234}
     * @return
     */
    Result sendMsg(String mobiles, String action, Map<String, Object> dataMap);

}
