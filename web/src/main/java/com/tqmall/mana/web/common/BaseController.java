package com.tqmall.mana.web.common;

import com.tqmall.mana.beans.BO.sys.ResourceBO;
import com.tqmall.mana.beans.BO.sys.UserBO;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.biz.manager.sys.SysBiz;
import com.tqmall.mana.component.util.mana.ManaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @Value("${idp.url}")
    protected String IDP_URL;
    @Value("${environment.version}")
    protected String environment;

    @Autowired
    private ShiroBiz shiroBiz;
    @Autowired
    private SysBiz sysBiz;


    @ModelAttribute
    public void setReqAndRes(HttpServletRequest req, HttpServletResponse res){
        if(ManaUtil.isAjaxRequest(req)){
            return;
        }

        request = req;
        response = res;
        session = req.getSession();

        //统一在这里，设置菜单id，用于ftl页面中选中菜单，具体逻辑看 leftMenu.ftl
        String menuIdStr = req.getParameter("menuId");
        Integer menuId = null;
        if(menuIdStr != null){
            menuId = Integer.valueOf(menuIdStr);
            session.setAttribute("menuId", menuId);
        }else{
            menuId = (Integer)session.getAttribute("menuId");
        }

        //openid url
        req.setAttribute("IDP_URL", IDP_URL);
        req.setAttribute("environment", environment);

        //获取当前登录用户信息
        UserBO userBO = shiroBiz.getCurrentUser();
        if(userBO != null){
            String userName = userBO.getUserName();
            List<ResourceBO> userMenuList = sysBiz.getUserMenuResource(userName, menuId);
            req.setAttribute("userMenuList", userMenuList);

        }

    }

}
