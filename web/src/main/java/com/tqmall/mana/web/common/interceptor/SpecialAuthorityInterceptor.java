package com.tqmall.mana.web.common.interceptor;

import com.tqmall.mana.component.bean.ConstantBean;
import com.tqmall.mana.component.util.UserUtil;
import com.tqmall.mana.component.util.mana.ManaUtil;
import com.tqmall.mana.web.common.annotation.SpecialAuthority;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * Created by huangzhangting on 16/12/7.
 */
public class SpecialAuthorityInterceptor extends HandlerInterceptorAdapter {
    @Value("${send.coupon.key}")
    private String sendCouponKey;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod method = (HandlerMethod) handler;
            SpecialAuthority specialAuthority = method.getMethodAnnotation(SpecialAuthority.class);
            if(specialAuthority != null){
                String key;
                Object obj = request.getSession().getAttribute(ConstantBean.SEND_COUPON_KEY);
                if(obj != null){
                    key = obj.toString();
                }else{
                    key = request.getHeader(ConstantBean.SEND_COUPON_KEY);
                }
                if(!sendCouponKey.equals(key)){
                    if(ManaUtil.isAjaxRequest(request)){
                        String jsonStr = UserUtil.unauthorizedJsonMsg();
                        response.setContentType("application/json; charset=utf-8");
                        response.setCharacterEncoding("UTF-8");

                        OutputStream out = response.getOutputStream();
                        out.write(jsonStr.getBytes("UTF-8"));
                        out.flush();
                        out.close();
                    }else{
                        response.sendRedirect("/common/unauthorized");
                    }
                }
            }
        }
        return true;
    }
}
