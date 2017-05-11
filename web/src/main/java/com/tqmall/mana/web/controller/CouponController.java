package com.tqmall.mana.web.controller;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.coupon.SearchCouponSendLogBO;
import com.tqmall.mana.beans.BO.coupon.SendCouponBO;
import com.tqmall.mana.beans.entity.coupon.ManaCouponSendLogDO;
import com.tqmall.mana.beans.entity.coupon.ManaCouponTypeDO;
import com.tqmall.mana.biz.manager.coupon.CouponBiz;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.component.bean.DataError;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by huangzhangting on 16/12/6.
 */
@Slf4j
@Controller
@RequestMapping("coupon")
public class CouponController extends BaseController {
    @Autowired
    private CouponBiz couponBiz;
    @Autowired
    private ShiroBiz shiroBiz;


//    @SpecialAuthority
    @RequestMapping("")
    public ModelAndView index(){
        ModelAndView view = new ModelAndView("mana/view/coupon");

        //todo 暂时注销掉这个逻辑，有点问题
//        Object key = session.getAttribute(ConstantBean.SEND_COUPON_KEY);
//        view.addObject(ConstantBean.SEND_COUPON_KEY, key);
//        session.removeAttribute(ConstantBean.SEND_COUPON_KEY);

        return view;
    }

//    @SpecialAuthority
    @RequestMapping(value = "sendCoupon", method = RequestMethod.POST)
    @ResponseBody
    public Result sendCoupon(SendCouponBO sendCouponBO){
        return couponBiz.sendCoupon(sendCouponBO);
    }

    @RequestMapping("getAllTypes")
    @ResponseBody
    public Result<List<ManaCouponTypeDO>> getAllTypes(){
        List<ManaCouponTypeDO> list = couponBiz.getAllTypes();
        if(list.isEmpty()){
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }
        return ResultUtil.successResult(list);
    }

//    @SpecialAuthority
    @RequestMapping("getSendLogPage")
    @ResponseBody
    public PagingResult<ManaCouponSendLogDO> getSendLogPage(SearchCouponSendLogBO searchCouponSendLogBO){
        log.info("{} getSendLogPage", shiroBiz.getCurrentUserRealName());
        return couponBiz.getSendLogPage(searchCouponSendLogBO);
    }

//    @SpecialAuthority
    @RequestMapping("exportSendLog")
    public void exportSendLog(String startDateStr, String endDateStr){
        log.info("{} exportSendLog, startDate:{}, endDate:{}", shiroBiz.getCurrentUserRealName(), startDateStr, endDateStr);

        SearchCouponSendLogBO searchCouponSendLogBO = new SearchCouponSendLogBO();
        if(!StringUtils.isEmpty(startDateStr)){
            searchCouponSendLogBO.setStartDateStr(startDateStr);
        }
        if(!StringUtils.isEmpty(endDateStr)) {
            searchCouponSendLogBO.setEndDateStr(endDateStr);
        }

        couponBiz.exportSendLog(response, searchCouponSendLogBO);
    }

}
