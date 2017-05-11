package com.tqmall.mana.component.annotation.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zxg on 17/2/4.
 * 16:19
 * no bug,以后改代码的哥们，祝你好运~！！
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManaLock {

    /**
     * 锁名称，
     * 若下面方法有参数，则会使用cacheName+"_"+@ManaCacheKey,
     * 若cacheName无，以方法名为key
     * @return redis key
     */
    String lockKeyName() default "";
}
