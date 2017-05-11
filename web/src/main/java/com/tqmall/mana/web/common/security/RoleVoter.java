package com.tqmall.mana.web.common.security;

import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.component.bean.ConstantBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * URL访问权限控制
 *
 * @author lixiao
 *         <p/>
 *         2014年12月30日 from legent-manager
 * update by zxg 2015/12/23
 */
@Slf4j
public class RoleVoter implements AccessDecisionVoter<FilterInvocation> {
    @Autowired
    private ShiroBiz shiroBiz;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation fi,
                    @SuppressWarnings("rawtypes") Collection attributes) {

        String url = fi.getRequestUrl();
        if(ConstantBean.OPENID_CONNECT_LOGIN.equals(url)){
            log.info("is openid_connect_login url");
            return ACCESS_GRANTED;
        }

        HttpServletRequest request = fi.getRequest();
        String queryStr = request.getQueryString();
        log.info("request record, url:{}, queryStr:{}", url, queryStr);

        Object obj = shiroBiz.getCurrentUser();
        if(obj == null){
            return ACCESS_DENIED;
        }
        return ACCESS_GRANTED;
    }

}
