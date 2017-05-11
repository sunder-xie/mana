package com.tqmall.mana.biz.interceptor;

import com.tqmall.mana.component.annotation.lock.ManaLock;
import com.tqmall.mana.component.annotation.lock.ManaLockKey;
import com.tqmall.mana.component.redis.RedisKeyBean;
import com.tqmall.mana.component.redis.lock.LockCallBack;
import com.tqmall.mana.component.redis.lock.RedisLockClient;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by zxg on 17/2/4.
 * 16:18
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class ManaLockInterceptor implements MethodInterceptor {

    @Autowired
    private RedisLockClient redisLockClient;
    @Override
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        //判断是不是接口,是接口类获得其实现类的method
        if(method.getDeclaringClass().isInterface()) {
            Object object = methodInvocation.getThis();
            String className = object.toString().split("@")[0];
            Class ImplClasss = Class.forName(className);
            method = ImplClasss.getDeclaredMethod(method.getName(),method.getParameterTypes());
        }

        ManaLock manaLock = AnnotationUtils.findAnnotation(method, ManaLock.class);
        String lockKeyName = manaLock.lockKeyName();

        String lockKey = getLockKey(methodInvocation, method, lockKeyName);

        Object result = redisLockClient.lock(lockKey, new LockCallBack() {
            @Override
            public Object onTask() {
                try {
                    return methodInvocation.proceed();
                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
                    if(throwable instanceof IllegalArgumentException){
                        throw new IllegalArgumentException(throwable.getMessage(), throwable.getCause());
                    }
                    throw new RuntimeException(throwable.getMessage(), throwable.getCause());
                }
            }
        });


        return result;
    }


    private String getLockKey(MethodInvocation methodInvocation,Method method,String lockName) throws NoSuchMethodException {
        StringBuilder lockKeyBuilder = new StringBuilder();

        Object[] args = methodInvocation.getArguments();//获得参数和对象类型

        if(StringUtils.isEmpty(lockName)){
            String methodName = method.getDeclaringClass().getName()+"."+method.getName();//methodName
            lockKeyBuilder.append(RedisKeyBean.SYSTEM_PREFIX).append(methodName);
        }else if(!lockName.startsWith(RedisKeyBean.SYSTEM_PREFIX)){
            lockKeyBuilder.append(RedisKeyBean.SYSTEM_PREFIX).append(lockName);
        }else {
            lockKeyBuilder.append(lockName);
        }

        // 尾部添加参数
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        int paramAnnLength = parameterAnnotations.length;
        for (int i = 0; i < paramAnnLength; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            if(annotations != null && annotations.length!= 0){
                for (Annotation annotation : annotations) {
                    if(annotation instanceof ManaLockKey){
                        lockKeyBuilder.append("_").append(args[i].toString());
                    }
                }
            }
        }

        return lockKeyBuilder.toString();
    }
}
