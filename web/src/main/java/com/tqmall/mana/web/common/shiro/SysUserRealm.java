package com.tqmall.mana.web.common.shiro;

import com.tqmall.mana.biz.manager.sys.SysBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huangzhangting on 15/9/11.
 */
@Slf4j
public class SysUserRealm extends AuthorizingRealm {
    @Autowired
    private SysBiz sysBiz;


    //授权（针对配置了权限的每一次请求，都会调用这个方法）
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = principalCollection.getPrimaryPrincipal().toString();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        /* 暂时没用到
        authorizationInfo.setRoles(new HashSet<String>());
        */

        authorizationInfo.setStringPermissions(sysBiz.getUserPermissions(userName));

        return authorizationInfo;
    }

    //认证登陆
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = authenticationToken.getPrincipal().toString();

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, "", getName());

        return authenticationInfo;
    }

}
