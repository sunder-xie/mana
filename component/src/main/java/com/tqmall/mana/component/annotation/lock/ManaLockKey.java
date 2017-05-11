package com.tqmall.mana.component.annotation.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zxg on 17/1/10.
 * 22:27
 * no bug,以后改代码的哥们，祝你好运~！！
 * 表示这个注解的变量参与redis的key值
 * 注意：这个注解的参数，必须有 toString 函数
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManaLockKey {
}
