package com.tqmall.mana.web.common.security;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.sys.UserBO;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.OIDCAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 登录认证filter
 * Created by yi.yang on 2014/7/30. from legent-manager
 * update by zxg 2015/12/23
 */
public class AuthenticationFilter extends org.mitre.openid.connect.client.OIDCAuthenticationFilter {

    @Value("${idp.url}")
    private String idpUrl;

    @Autowired
    private ShiroBiz shiroBiz;


    @Override
    protected Authentication handleAuthorizationCodeResponse(HttpServletRequest request,
                                                             HttpServletResponse response) {

        Authentication authentication = null;
        try {
            authentication = super.handleAuthorizationCodeResponse(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (authentication == null) {
            try {
                response.sendRedirect(request.getContextPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        if (!(authentication instanceof OIDCAuthenticationToken)) {
            return authentication;
        }

        DefaultUserInfo userInfo =
                (DefaultUserInfo) ((OIDCAuthenticationToken) authentication).getUserInfo();

        if (userInfo == null) {
            return authentication;
        }

        String staffNo = userInfo.getStaffNo();

        if(!StringUtils.isEmpty(staffNo)) {
            Result result = shiroBiz.userLogin(staffNo);
            if(result.isSuccess()){
                UserBO userBO = new UserBO();
                userBO.setUserName(staffNo);
                userBO.setRealName(userInfo.getUserName());
                shiroBiz.setCurrentUser(userBO);
            }
        }
        return authentication;

    }
}
