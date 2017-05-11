package com.tqmall.mana.biz.util;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.tqmall.mana.beans.entity.insurance.OtherCityDO;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zxg on 16/12/5.
 * 10:31
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class OtherCityUtils {
    //provinceName+sourceName:provinceCode
    private static String PROVINCE_KEY = "%s-%s";
    private static Map<String,String> provinceMap = new ConcurrentHashMap<>();
    //city_name+sourceName:city_code
    private static String CITY_KEY = "%s-%s";
    private static Map<String,String> cityMap = new ConcurrentHashMap<>();
    // sourceName:codeSet
    private static Map<String,Set<String>> codeMap = new ConcurrentHashMap<>();

    // 通过省份名称获得编码
    public static String getCodeByProvinceName(String provinceName,String sourceName){
        if(!StringUtils.isEmpty(provinceName) &&  !StringUtils.isEmpty(sourceName)){
            for (Map.Entry<String,String> provinceEntry:provinceMap.entrySet()){
                String key = provinceEntry.getKey();
                // 省份包含即可
                if(key.contains(provinceName) && key.contains(sourceName)){
                    return provinceEntry.getValue();
                }
            }
        }
        return null;
    }

    // 通过city名称获得其编码
    public static String getCodeByCityName(String cityName, String sourceName){
        if(StringUtils.isEmpty(cityName) || StringUtils.isEmpty(sourceName)){
            return null;
        }

        String key = String.format(CITY_KEY, cityName,sourceName);
        if(cityMap.containsKey(key)){
            return cityMap.get(key);
        }
        return null;
    }

    // 此编码是否存在
    public static Boolean isExistCode(String theCode,String sourceName){
        if(StringUtils.isEmpty(theCode)){
            return false;
        }
        Boolean isExist = Boolean.FALSE;
        for(Map.Entry<String,Set<String>> entry:codeMap.entrySet()){
            String key = entry.getKey();
            Set<String> valueSet = entry.getValue();
            if(StringUtils.isEmpty(sourceName) || key.equals(sourceName)){
                isExist = valueSet.contains(theCode);
                if(isExist){
                    // 如果找到，则跳出
                    break;
                }
            }
        }

        return isExist;
    }


    public static Map<String,Object> getMap(){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("provinceMap",provinceMap);
        resultMap.put("cityMap",cityMap);
        resultMap.put("codeMap",codeMap);
        return resultMap;
    }

    public static void setMap(List<OtherCityDO> otherCityDOList){
        Map<String,String> provinceHelpMap = new ConcurrentHashMap<>();
        Map<String,String> cityHelpMap = new ConcurrentHashMap<>();
        Map<String,Set<String>> codeHelpMap = new ConcurrentHashMap<>();

        for(OtherCityDO otherCityDO:otherCityDOList){
            String sourceName = otherCityDO.getSourceName();
            String provinceCode = otherCityDO.getProvinceCode();
            provinceHelpMap.put(String.format(PROVINCE_KEY,otherCityDO.getProvinceName(),sourceName), provinceCode);

            String cityName = otherCityDO.getCityName();
            String cityAllName = otherCityDO.getCityAllName();
            String cityCode = otherCityDO.getCityCode();
            cityHelpMap.put(String.format(CITY_KEY, cityName,sourceName), cityCode);
            cityHelpMap.put(String.format(CITY_KEY, cityAllName,sourceName), cityCode);

            // 插入code
            Set<String> codeSet = codeHelpMap.get(sourceName);
            if(codeSet == null){
                codeSet = new ConcurrentHashSet<>();
                codeHelpMap.put(sourceName,codeSet);
            }
            codeSet.add(provinceCode);
            codeSet.add(cityCode);
        }

        provinceMap = provinceHelpMap;
        cityMap = cityHelpMap;
        codeMap = codeHelpMap;
    }
}
