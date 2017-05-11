package com.tqmall.mana.web.common.shiro;


import com.tqmall.mana.component.util.UserUtil;
import com.tqmall.mana.component.util.mana.ManaUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 登录过滤器
 * Created by huangzhangting on 16/7/15.
 */
public class SysUserFilter extends UserFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //判断是否登录请求
        if (isLoginRequest(request, response)) {
            return true;
        }
        Subject subject = getSubject(request, response);
        // If principal is not null, then the user is known and should be allowed access.
        if(subject.getPrincipal() != null){
            return true;
        }

        return false;
    }

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        //判断ajax请求
        if(ManaUtil.isAjaxRequest((HttpServletRequest) request)){
            response.setContentType("application/json; charset=utf-8");
            response.setCharacterEncoding("UTF-8");

            String jsonStr = UserUtil.notLoginJsonMsg();
            OutputStream out = response.getOutputStream();
            out.write(jsonStr.getBytes("UTF-8"));
            out.flush();
            out.close();

        }else {
            WebUtils.issueRedirect(request, response, getLoginUrl());
        }
    }

}
