package com.tqmall.mana.component.util;

/**
 * Created by huangzhangting on 16/12/19.
 */
public class UserUtil {

    public static String notLoginJsonMsg(){
        return "{\"data\":null,\"success\":false,\"code\":\"99999999\",\"message\":\"请先登录\"}";
    }

    public static String unauthorizedJsonMsg(){
        return "{\"data\":null,\"success\":false,\"code\":\"99999995\",\"message\":\"您没有权限操作\"}";
    }

}
