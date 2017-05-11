package com.tqmall.mana.component.redis.lock;

/**
 * Created by zxg on 16/12/19.
 * 15:09
 * no bug,以后改代码的哥们，祝你好运~！！
 * 锁机制，获得锁后的 实际业务代码块
 */
public interface LockCallBack {
    Object onTask();
}
