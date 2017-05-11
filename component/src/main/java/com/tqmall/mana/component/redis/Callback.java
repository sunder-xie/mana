package com.tqmall.mana.component.redis;

import redis.clients.jedis.ShardedJedis;

/**
 * Created by zxg on 16/12/19.
 * 15:09
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public interface Callback {

    Object onTask(ShardedJedis shardedJedis) throws InterruptedException;
}
