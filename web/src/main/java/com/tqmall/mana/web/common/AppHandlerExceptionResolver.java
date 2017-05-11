package com.tqmall.mana.web.common;

import com.tqmall.mana.component.exception.BusinessCheckException;
import com.tqmall.mana.component.exception.BusinessException;
import com.tqmall.mana.component.util.mana.ManaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局性的处理controller层出现的异常
 * Created by huangzhangting on 16/1/27.
 */
@Slf4j
@Component
public class AppHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) {
        /**
         * 对无法控制的ClientAbort异常, 不做日志打印
         */
        if (!"org.apache.catalina.connector.ClientAbortException".equals(ex.getClass().getCanonicalName())) {
            log.error("Spring Controller invoker error.", ex);
        }

        ModelAndView view = new ModelAndView();

        if(ManaUtil.isAjaxRequest(req)){
            String message = "系统内部发生错误，请联系技术人员。";
            if(ex instanceof BusinessException || ex instanceof BusinessCheckException
                    || ex instanceof IllegalArgumentException){
                message = ex.getMessage();
            }
            view.addObject("success", false);
            view.addObject("message", message);

            view.setView(new MappingJackson2JsonView());

        }else{
            view.setViewName("500");
        }

        return view;
    }
}
