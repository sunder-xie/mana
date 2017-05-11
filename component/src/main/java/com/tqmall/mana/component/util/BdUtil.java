package com.tqmall.mana.component.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * do 和 bo 的互相转化类
 */
@Slf4j
public class BdUtil {

    public static <DO, BO> BO do2bo(DO request, Class<BO> cls) {
        if (null == request) return null;
        BO result = null;
        try {
            result = cls.newInstance();
            BeanUtils.copyProperties(request, result);
        } catch (Exception e) {
            log.error("对象copy失败，请检查相关module", e);
        }
        return result;
    }

    public static <DO, BO> List<BO> do2bo4List(Collection<DO> request, Class<BO> cls) {
        List<BO> result = Lists.newArrayList();
        if(request == null){
            return result;
        }
        for (DO obj : request) {
            BO bo = do2bo(obj, cls);
            if(bo!=null){
                result.add(bo);
            }
        }
        return result;
    }

    public static <DO, BO, K> Map<K, BO> do2bo4Map(Map<K, DO> request, Class<BO> cls) {
        Map<K, BO> result = Maps.newHashMap();
        for (Map.Entry<K, DO> item : request.entrySet()) {
            K key = item.getKey();
            DO val = item.getValue();
            result.put(key, do2bo(val, cls));
        }
        return result;
    }

}
