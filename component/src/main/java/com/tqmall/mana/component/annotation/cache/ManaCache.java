package com.tqmall.mana.component.annotation.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zxg on 17/1/10.
 * 22:27
 * no bug,以后改代码的哥们，祝你好运~！！
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManaCache {
    /**
     * 缓存名称，
     * 若下面方法有参数，则会使用cacheName+"_"+@ManaCacheKey,
     * 若cacheName无，以方法名为key
     * @return redis key
     */
    String cacheName() default "";

    int cacheTime() default 3600;//缓存失效时间，默认 1h,若为0，则永久缓存
}
