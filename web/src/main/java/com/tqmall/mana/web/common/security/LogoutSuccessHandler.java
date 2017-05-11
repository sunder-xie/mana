package com.tqmall.mana.web.common.security;

import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import lombok.extern.slf4j.Slf4j;
import org.mitre.openid.connect.model.OIDCAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出后需要远程退出openid
 * Created by yi.yang on 2014/7/31. from legent-manager
 * update by zxg 2015/12/23
 */
/*此处处理为Controller，为了前端点击 登出的按钮，调用此方法，remove session中内容*/
@Slf4j
@RestController
@RequestMapping("/openidLogout")
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    @Autowired
    private ShiroBiz shiroBiz;


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        if(authentication instanceof OIDCAuthenticationToken){
            //退出系统
            shiroBiz.userLogout();

        }

        //随便重定向到一个路径即可，因为已经退出openid，自动会跳转到登录页面
        response.sendRedirect("/");
    }
}
