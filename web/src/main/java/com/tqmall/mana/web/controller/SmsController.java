package com.tqmall.mana.web.controller;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.entity.sms.ManaSmsTemplateDO;
import com.tqmall.mana.biz.manager.sms.SmsBiz;
import com.tqmall.mana.component.bean.DataError;
import com.tqmall.mana.component.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by huangzhangting on 16/12/9.
 */
@Controller
@RequestMapping("sms")
public class SmsController {
    @Autowired
    private SmsBiz smsBiz;

    @RequestMapping("getAllTemplate")
    @ResponseBody
    public Result<List<ManaSmsTemplateDO>> getAllTemplate(){
        List<ManaSmsTemplateDO> list =  smsBiz.getAllTemplate();

        if(list.isEmpty()){
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }
        return ResultUtil.successResult(list);
    }

}
