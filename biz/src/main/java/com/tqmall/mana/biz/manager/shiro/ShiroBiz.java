package com.tqmall.mana.biz.manager.shiro;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.sys.UserBO;
import com.tqmall.mana.component.bean.ConstantBean;
import com.tqmall.mana.component.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

/**
 * Created by huangzhangting on 16/12/19.
 */
@Slf4j
@Service
public class ShiroBiz {

    public Result userLogin(String userName){
        UsernamePasswordToken token = new UsernamePasswordToken(userName, "");
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(token);
            log.info("user login success, userName:{}", userName);
            return ResultUtil.successResult(userName);
        }catch (AuthenticationException ae){
            log.info("user login failed, userName:{}, message:{}", userName, ae.getMessage());
            return ResultUtil.errorResult("", ae.getMessage());
        }catch (Exception e){
            log.error("user login error, userName:"+userName, e);
            return ResultUtil.errorResult("", "登录失败，系统内部发生错误");
        }

    }
    public void userLogout(){
        UserBO userBO = getCurrentUser(); //TODO 事实上，并拿不到数据，待继续调试
        if(userBO != null){
            log.info("user logout, userName:{}, realName:{}", userBO.getUserName(), userBO.getRealName());
        }
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    public String getCurrentUserRealName(){
        UserBO userBO = getCurrentUser();
        if(userBO != null){
            return userBO.getRealName();
        }
        return null;
    }
    public UserBO getCurrentUser(){
        Object obj = getFromSession(ConstantBean.CURRENT_USER_KEY);
        if(obj != null){
            return (UserBO)obj;
        }
        return null;
    }
    public void setCurrentUser(UserBO user){
        putIntoSession(ConstantBean.CURRENT_USER_KEY, user);
    }


    public Session getSession(){
        Subject subject = SecurityUtils.getSubject();
        return subject.getSession();
    }
    public void putIntoSession(String key, Object val){
        Session session = getSession();
        session.setAttribute(key, val);
    }
    public Object getFromSession(String key){
        Session session = getSession();
        return session.getAttribute(key);
    }
    public Object removeFromSession(String key){
        Session session = getSession();
        return session.removeAttribute(key);
    }

}
