package com.tqmall.mana.web.controller;

import com.tqmall.mana.web.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhouheng on 17/2/15.
 */
@Controller
@RequestMapping("service")
public class ServiceController  extends BaseController{

    @RequestMapping("itemListInit")
    public ModelAndView itemListInit(){

        ModelAndView modelAndView = new ModelAndView("mana/view/service/itemList");

        return modelAndView;
    }

    @RequestMapping("addItemInit")
    public ModelAndView addItemInit(){

        ModelAndView modelAndView = new ModelAndView("mana/view/service/addItem");

        return modelAndView;
    }

    @RequestMapping("packageListInit")
    public  ModelAndView packageListInit(){

        ModelAndView modelAndView = new ModelAndView("mana/view/service/packageList");

        return modelAndView;
    }

    @RequestMapping("addPackageInit")
    public ModelAndView addPackageInit(){

        ModelAndView modelAndView = new ModelAndView("mana/view/service/addPackage");

        return modelAndView;
    }


}
