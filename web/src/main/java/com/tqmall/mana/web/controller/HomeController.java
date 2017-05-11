package com.tqmall.mana.web.controller;

import com.tqmall.mana.web.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 主页
 * Created by huangzhangting on 16/12/1.
 */
@Controller
@RequestMapping("home")
public class HomeController extends BaseController {

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView("mana/view/homePage");

        return view;
    }

}
