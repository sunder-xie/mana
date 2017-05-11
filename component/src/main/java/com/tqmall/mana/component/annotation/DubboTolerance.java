package com.tqmall.mana.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zxg on 16/12/9.
 * 10:55
 * no bug,以后改代码的哥们，祝你好运~！！
 * service容错的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DubboTolerance {

}
