package com.tqmall.mana.component.redis.lock;

/**
 * Created by zxg on 16/12/19.
 * 15:07
 * no bug,以后改代码的哥们，祝你好运~！！
 * 基础redis 的 分布式锁机制
 */
public interface RedisLockClient {
    // 锁住代码块，执行完后解锁
    Object lock(String key, LockCallBack callback);

    //判断锁是否存在，若存在，等待，在其不存在时执行
    Object doIfNotLock(String key, LockCallBack callback);
}
