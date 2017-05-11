package com.tqmall.mana.component.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by huangzhangting on 17/4/17.
 */
public class CommUtils {
    public static final String[] HEADS = {"X-Forwarded-For","X-Real-IP"};

    /**
     * 获取真实的ip
     * @param request
     * @return
     */
    public static String getRealIp(HttpServletRequest request){
        for(String h : HEADS){
            String ip = request.getHeader(h);
            if(ip!=null && ip.length()>0){
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

}
