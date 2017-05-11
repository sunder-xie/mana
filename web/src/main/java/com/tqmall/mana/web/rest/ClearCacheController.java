package com.tqmall.mana.web.rest;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.component.redis.RedisClientTemplate;
import com.tqmall.mana.component.redis.RedisKeyBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * 缓存清理controller
 *
 * Created by huangzhangting on 16/12/12.
 */
@Slf4j
@Controller
@RequestMapping("clearCache")
public class ClearCacheController {
//    @Autowired
//    private EhCacheCacheManager ehCacheCacheManager;
    @Autowired
    private RedisClientTemplate redisClient;

    /**
     * 清理全部的ehcache缓存
     * @return
     */
//    @RequestMapping("clearAllEhCache")
//    @ResponseBody
//    public Result clearAllEhCache(){
//        CacheManager cacheManager = ehCacheCacheManager.getCacheManager();
//        cacheManager.clearAll();
//        return Result.wrapSuccessfulResult("清理全部ehcache成功");
//    }


    /**
     * 根据表达式清除redis缓存
     * 例子：MaNa_user_permissions_* ，也可以是具体值
     * @param pattern
     * @return
     */
    @RequestMapping("clearRedisCache")
    @ResponseBody
    public Result clearRedisCache(String pattern){
        if(pattern==null){
            return Result.wrapErrorResult("", "参数不能为空");
        }
        if(!pattern.startsWith(RedisKeyBean.SYSTEM_PREFIX) || pattern.startsWith(RedisKeyBean.SYSTEM_PREFIX+"*")){
            return Result.wrapErrorResult("", "参数不合法, pattern: "+pattern);
        }

        //获取需要删除key
        Set<String> keys = redisClient.getKeys(pattern);
        if(keys.isEmpty()){
            return Result.wrapErrorResult("", "没有相关缓存, pattern: "+pattern);
        }

        //验证key
        List<String> delKeyList = new ArrayList<>();
        for(String key : keys){
            if(!isUnFreshKey(key)){
                delKeyList.add(key);
            }
        }

        return Result.wrapSuccessfulResult(redisClient.delKeys(delKeyList.toArray(new String[delKeyList.size()])));

    }
    //检验是否不可刷缓存的key
    private boolean isUnFreshKey(String checkKey){
//        for(String key : RedisKeyBean.UN_FRESH_KEY_SET) {
//            String keyReg = key.replaceAll("%s", ".+").replaceAll("%d", ".+");
//            Pattern pattern = Pattern.compile(keyReg);
//            Matcher matcher = pattern.matcher(checkKey);
//            if(matcher.matches()){
//                return true;
//            }
//        }
        return false;
    }

}
