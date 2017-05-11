package com.tqmall.mana.component.util;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by huangzhangting on 15/8/27.
 */
@Slf4j
public class JsonUtil {
    private static ObjectMapper OM = new ObjectMapper();

    /**
     * 对象转换成json字符串
     * @param object
     * @return
     */
    public static String objectToStr(Object object){
        if(object==null){
            return null;
        }

        try {
            return OM.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("object to string error, object:"+object, e);
        }
        return null;
    }

    /**
     * json字符串转换成对象
     * @param content
     * @param valueType
     * @param <T>
     * @return
     */
    public static <T> T strToObject(String content, Class<T> valueType){
        try {
            OM.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            return OM.readValue(content, valueType);
        } catch (IOException e) {
            log.error("string to object error, content:" + content, e);
        }
        return null;
    }

    public static <T> T strToObject(String content, JavaType valueType){
        try {
            return OM.readValue(content, valueType);
        } catch (IOException e) {
            log.error("string to object error, content:" + content, e);
        }
        return null;
    }
    public static <T> T strToObject(String content, Type valueType){
        try {
            JavaType javaType = getJavaType(valueType);
            if(javaType == null){
                log.error("can't support this returnType.type:" + valueType );
                return null;
            }
            return OM.readValue(content, javaType);
        } catch (IOException e) {
            log.error("string to object error, content:" + content, e);
        }
        return null;
    }



    /**
     * json字符串，转换成集合
     * @param content json数组
     * @param collectionClass 集合class 例如 List.class
     * @param elementClass 元素class 例如 User.class
     * @param <T>
     * @return
     */
    public static <T> T strToCollection(String content, Class<? extends Collection> collectionClass, Class<?> elementClass){
        JavaType javaType = getCollectionType(collectionClass, elementClass);
        try {
            return OM.readValue(content, javaType);
        } catch (IOException e) {
            log.error("string to collection error, content:"+content, e);
        }
        return null;
    }

    public static <T> List<T> strToList(String content, Class<?> elementClass) {
        List<T> list = strToCollection(content, List.class, elementClass);
        if(list==null){
            return new ArrayList<>();
        }
        return list;
    }



    public static <T> T strToMap(String content, Class<? extends Map> mapClass,Class<?> keyClass,Class<?> valueClass){
        JavaType javaType = getMapType(mapClass, keyClass, valueClass);
        try {
            return OM.readValue(content, javaType);
        } catch (IOException e) {
            log.error("string to map error, content:"+content, e);
        }
        return null;
    }

    /*
    *  JsonNode：就是json数组对象
    *  collectionClass：集合class 例如 List.class
    *  elementClass：元素class 例如 User.class
    * */
    public static <T> T jsonNodeToCollection(JsonNode jsonNode, Class<? extends Collection> collectionClass, Class<?> elementClass){
        JavaType javaType = getCollectionType(collectionClass, elementClass);
        try {
            return OM.readValue(jsonNode.toString(), javaType);
        } catch (IOException e) {
            log.error("JsonNode to collection error", e);
        }
        return null;
    }

    /*
    *  将json字符串转换成json对象
    *
    *  JsonNode：就是json对象，包括数组、或单个对象
    *  content：json字符串
    *
    *  可以通过 jsonNode.findValue("pro") 查找属性值，返回值还是 JsonNode
    * */
    public static JsonNode getJsonNode(String content){
        try {
            return OM.readTree(content);
        } catch (IOException e) {
            log.error("get JsonNode error, content:"+content, e);
        }
        return null;
    }

    /*
    *  获得java集合类型，json在转化成java集合时需要用到
    *  collectionClass：集合class 例如 List.class
    *  elementClass：元素class 例如 User.class
    * */
    public static JavaType getCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass){
        return OM.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }
    public static JavaType getCollectionType(Class<? extends Collection> collectionClass, JavaType elementType){
        return OM.getTypeFactory().constructCollectionType(collectionClass, elementType);
    }

    public static JavaType getMapType(Class<?> mapClass,Class<?> keyClass,Class<?> valueClass){
        return OM.getTypeFactory().constructMapLikeType(mapClass, keyClass, valueClass);
    }
    public static JavaType getMapType(Class<?> mapClass, JavaType keyType, JavaType valueType){
        return OM.getTypeFactory().constructMapType((Class<? extends Map>) mapClass, keyType, valueType);
    }
    public static JavaType getType(Class<?> theClass){
        return OM.getTypeFactory().constructType(theClass);
    }
    public static JavaType getType(Type thetype){
        return OM.getTypeFactory().constructType(thetype);
    }



    public static JavaType getJavaType(Type type){
        /**//* 如果是泛型类型 */
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();// 泛型类型列表
            Class returnTypeName = (Class) ((ParameterizedType) type).getRawType();
            int typeLength = types.length;
            if (typeLength < 1 || typeLength > 2) {
                log.error("can't support this returnType.type:" + type + ",types:" + Arrays.toString(types));
                return null;
            }
            // Collection
            if (typeLength == 1) {
                JavaType argType = getJavaType(types[0]);
                return JsonUtil.getCollectionType(returnTypeName, argType);
            }
            // map
            if (typeLength == 2) {
                JavaType keyType = getJavaType(types[0]);
                JavaType valueType = getJavaType(types[1]);
                return JsonUtil.getMapType(returnTypeName, keyType, valueType);
            }
        }else{
            // 跳出递归
            return JsonUtil.getType(type);
        }
        return null;
    }
}
