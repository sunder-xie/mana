package com.tqmall.mana.biz.manager.test;

import com.google.common.collect.Lists;
import com.tqmall.mana.component.annotation.cache.ManaCacheKey;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zxg on 17/1/10.
 * 23:03
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Service
public class TestCacheBiz {

//    @ManaCache(cacheName = RedisKeyBean.SYSTEM_PREFIX+"_testString",cacheTime = RedisKeyBean.RREDIS_EXP_DAY)
    public String testString(String a,@ManaCacheKey int b){
        String result = a+"_2131";
        return result;
    }


//    @ManaCache
    public List<String> testList(@ManaCacheKey long c){
        List<String> resultList = Lists.newArrayList("121","123");
        return resultList;
    }

}
