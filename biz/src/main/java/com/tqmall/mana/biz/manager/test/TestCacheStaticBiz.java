package com.tqmall.mana.biz.manager.test;

import com.tqmall.mana.component.annotation.cache.ManaCache;
import com.tqmall.mana.component.redis.RedisKeyBean;

/**
 * Created by zxg on 17/1/18.
 * 10:43
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class TestCacheStaticBiz {
    @ManaCache(cacheName = RedisKeyBean.SYSTEM_PREFIX+"test_static", cacheTime = 0)
    public static String getResult(){
        //redis 缓存不生效的，因为是 static形式
        return "1231";
    }
}
