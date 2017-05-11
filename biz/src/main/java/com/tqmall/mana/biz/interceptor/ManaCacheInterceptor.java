package com.tqmall.mana.biz.interceptor;

import com.tqmall.mana.component.annotation.cache.ManaCache;
import com.tqmall.mana.component.annotation.cache.ManaCacheKey;
import com.tqmall.mana.component.redis.RedisClientTemplate;
import com.tqmall.mana.component.redis.RedisKeyBean;
import com.tqmall.mana.component.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by zxg on 17/1/10.
 * 22:33
 * no bug,以后改代码的哥们，祝你好运~！！
 * 缓存的拦截器
 */
@Slf4j
public class ManaCacheInterceptor implements MethodInterceptor{

    @Autowired
    private RedisClientTemplate redisClientTemplate;
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        //判断是不是接口,是接口类获得其实现类的method
        if(method.getDeclaringClass().isInterface()) {
            Object object = methodInvocation.getThis();
            String className = object.toString().split("@")[0];
            Class ImplClasss = Class.forName(className);
            method = ImplClasss.getDeclaredMethod(method.getName(),method.getParameterTypes());
        }

        ManaCache manaCache = AnnotationUtils.findAnnotation(method, ManaCache.class);
        String cacheName = manaCache.cacheName();
        Integer cacheTime = manaCache.cacheTime() == 0 ? null : manaCache.cacheTime();

        String cacheKey = getCacheKey(methodInvocation,method, cacheName);
        String resultJson = redisClientTemplate.get(cacheKey);
        if(resultJson != null){
            if(redisClientTemplate.isNone(resultJson)){
                return null;
            }
            //  存在缓存，若Null，说明处理不了，不走缓存
            Object result = dataFromCache(method,resultJson);
            if(result != null) {
                return result;
            }
        }
        // 取实际数据
        Object result = methodInvocation.proceed();
        //存缓存
        saveDataToCache(result,cacheKey,cacheTime);

        return result;
    }

    private String getCacheKey(MethodInvocation methodInvocation,Method method,String cacheName) throws NoSuchMethodException {
        StringBuilder cacheKey = new StringBuilder();

        //获得参数和对象类型
        String methodName = method.getDeclaringClass().getName()+"."+method.getName();//methodName
        Object[] args = methodInvocation.getArguments();


        if(StringUtils.isEmpty(cacheName)){
            cacheKey.append(RedisKeyBean.SYSTEM_PREFIX).append(methodName);
        }else if(!cacheName.startsWith(RedisKeyBean.SYSTEM_PREFIX)){
            cacheKey.append(RedisKeyBean.SYSTEM_PREFIX).append(cacheName);
        }else {
            cacheKey.append(cacheName);
        }

        // 尾部添加参数
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        int paramAnnLength = parameterAnnotations.length;
        for (int i = 0; i < paramAnnLength; i++) {
           Annotation[] annotations = parameterAnnotations[i];
            if(annotations != null && annotations.length!= 0){
                for (Annotation annotation : annotations) {
                    if(annotation instanceof ManaCacheKey){
                        cacheKey.append("_").append(args[i].toString());
                    }
                }
            }
        }

        return cacheKey.toString();
    }


    private Object dataFromCache(Method method,String jsonStr){
        Type returnType = method.getGenericReturnType();
        return JsonUtil.strToObject(jsonStr, returnType);
    }

    private void saveDataToCache(Object saveObject,String key,Integer cacheTime){
        if(saveObject == null){
            redisClientTemplate.setNone(key);
            return;
        }

        redisClientTemplate.lazySet(key,saveObject,cacheTime);
    }


}
