package com.tqmall.mana.component.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by zengjinju on 17/1/14.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface IgnoreCheckAnnotation {
    //是否需要忽略check,默认不忽略
    boolean isIgnore() default false;
}
