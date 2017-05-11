package com.tqmall.mana.component.redis.lock;

import com.tqmall.mana.component.redis.Callback;
import com.tqmall.mana.component.redis.RedisClientTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by zxg on 16/12/19.
 * 15:14
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Service
@Slf4j
public class RedisLockClientImpl implements RedisLockClient {
//    private static final String LOCKET_KEY = "locket";
    private static final String LOCKET_TIME = "lockExpiresStr";
    // 若锁持有超时 10 min，则解锁
    private static final int expireMsecs = 10 * 60 * 1000;
    // 锁等待超时 50s，若超等待时间，则返回错误
    private static final int timeoutMsecs = 50 * 1000;

    private final Random r = new Random();


    @Resource
    private RedisClientTemplate redisClient;

    @Override
    public Object lock(final String lockKey, final LockCallBack callback) {
        if (StringUtils.isEmpty(lockKey)) {
            return false;
        }
        Object result = redisClient.runTask(new Callback() {
            @Override
            public Object onTask(ShardedJedis jedis) throws InterruptedException {
                return lock(jedis, lockKey, callback);
            }
        });

        return result;

    }

    @Override
    public Object doIfNotLock(final String lockKey, final LockCallBack callback) {
        if (StringUtils.isEmpty(lockKey)) {
            return null;
        }

        Object result = redisClient.runTask(new Callback() {
            @Override
            public Object onTask(ShardedJedis jedis) throws InterruptedException {
                int timeout = timeoutMsecs;
                while (timeout > 0) {
                    String currentValueStr = jedis.get(lockKey);
                    if (currentValueStr == null || Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                        //锁已经失效
                        return callback.onTask();
                    }
                    timeout -= 100;
                    // 短暂休眠，nano为了防止饥饿进程的出现
                    Thread.sleep(100, r.nextInt(500));
                }
                return null;
            }
        });

        return result;
    }

    private Object lock(ShardedJedis jedis, String lockKey, LockCallBack callback) throws InterruptedException {
//        Map<String,Object> lockMap = null;
        String expiresStr = null;
        try {
            expiresStr = acquire(jedis, lockKey);
            if (expiresStr != null) {
                // 执行业务逻辑
                return callback.onTask();
            }
        } catch (Exception e) {
//            log.error("Redis lock runTask error: ", e);
            throw e;
        } finally {
            // 释放锁
            release(jedis, lockKey, expiresStr);
            // jedis 的回收由redisClient 去做
        }
        return null;
    }

    // 获得锁
    private String acquire(ShardedJedis jedis, String lockKey) throws InterruptedException {
//        Map<String,Object> map = new HashMap<>();

        int timeout = timeoutMsecs;
        while (timeout > 0) {
            long expires = System.currentTimeMillis() + expireMsecs + 1;
            String expiresStr = String.valueOf(expires); //锁到期时间

            if (jedis.setnx(lockKey, expiresStr) == 1) {
//                map.put(LOCKET_KEY,true);
//                map.put(LOCKET_TIME,expiresStr);
                //获得锁，
                return expiresStr;
            }
            String currentValueStr = jedis.get(lockKey);// redis 中存的时间
            /* a.在此时若有 服务器释放了锁，则current 为null。进行下次遍历的操作
            ** b. 锁还在，且时间比当前时间要小，却超时了。老的锁还在，且 超时
            *  c. currentValueStr == null
             */
            if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                //获取上一个锁到期时间，并设置现在的锁到期时间
                String oldValueStr = jedis.getSet(lockKey, expiresStr);
                //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是原子性的
                // 若不相等，说明有个线程 已经早一步执行了getSet，把它时间后延一点，影响不大因此继续等待
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    //如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
//                    map.put(LOCKET_KEY,true);
//                    map.put(LOCKET_TIME,expiresStr);
                    //获得锁，
                    return expiresStr;
                }
                // key所在的锁已经被释放，此时被此线程设置值了，获得锁
                if (oldValueStr == null) {
//                    map.put(LOCKET_KEY,true);
//                    map.put(LOCKET_TIME,expiresStr);
                    //获得锁，
                    return expiresStr;
                }
            }
            timeout -= 100;
            // 短暂休眠，nano为了防止饥饿进程的出现
            Thread.sleep(100, r.nextInt(500));
        }
        throw new RuntimeException("获取redis分布式锁超时，lockKey="+lockKey);
    }

    // 解锁
    private void release(ShardedJedis jedis, String lockKey, String expiresStr) {
        if(expiresStr==null){
            return;
        }

        String currentValueStr = jedis.get(lockKey);
        if (currentValueStr != null) {
            long diff = Long.parseLong(currentValueStr) - Long.parseLong(expiresStr);
            /*
            * 1、正常情况diff=0
            * 2、由于 redis.getSet 竞争，导致diff>0（可能会小于0 ？）
            * 3、如果当前线程执超时了，并且锁被其他线程获得，则 diff > 超时时间
            * */
            if (diff < expireMsecs) {
                jedis.del(lockKey);
            } else {
                // 这个进程的锁超时了，被 新的进程锁获得替换了。则不进行任何操作。打印日志，方便后续跟进
                log.error("the lockKey over time.lockKey:{}.expireMsecs:{},over time is", lockKey, expireMsecs, System.currentTimeMillis() - Long.valueOf(expiresStr));
            }
        }
    }
}
