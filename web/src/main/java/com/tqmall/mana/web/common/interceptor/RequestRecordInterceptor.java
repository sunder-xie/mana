package com.tqmall.mana.web.common.interceptor;

import com.tqmall.mana.component.util.CommUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求记录拦截器
 * Created by huangzhangting on 16/7/28.
 */
@Slf4j
public class RequestRecordInterceptor extends HandlerInterceptorAdapter{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String host = request.getRemoteHost();
        String uri = request.getRequestURI();
        String qs = request.getQueryString();
        String realIp = CommUtils.getRealIp(request);
        log.info("request record, host:{}, realIp:{}, uri:{}, queryString:{}", host, realIp, uri, qs);
        return true;
    }
}
