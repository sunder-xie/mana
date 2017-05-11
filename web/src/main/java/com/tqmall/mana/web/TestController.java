package com.tqmall.mana.web;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.param.settle.SettleCompanyCheckDetailBizParam;
import com.tqmall.mana.biz.manager.customer.CustomerBiz;
import com.tqmall.mana.biz.manager.settle.checkDetail.SettleCompanyCheckDetailBiz;
import com.tqmall.mana.client.beans.param.ShopSettleDetailPackageModeQueryParam;
import com.tqmall.mana.client.service.settle.RpcSettleShopService;
import com.tqmall.mana.component.enums.insurance.dict.CooperationModeEnum;
import com.tqmall.mana.component.redis.lock.LockCallBack;
import com.tqmall.mana.component.redis.lock.RedisLockClient;
import com.tqmall.mana.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by huangzhangting on 16/11/29.
 */
@Controller
@RequestMapping("test")
public class TestController extends BaseController{
    @Autowired
    private CustomerBiz customerBiz;
    @Autowired
    private SettleCompanyCheckDetailBiz settleCompanyCheckDetailBiz;
    @Autowired
    private RedisLockClient redisLockClient;
    @Autowired
    private RpcSettleShopService rpcSettleShopService;


    @RequestMapping("testFtl")
    public ModelAndView testFtl(){
        return new ModelAndView("mana/test");
    }

    @RequestMapping("pageException")
    public ModelAndView pageException(){
        throw new RuntimeException("页面跳转异常");
    }

    @RequestMapping("ajaxExceptionTest")
    @ResponseBody
    public Object ajaxExceptionTest(){
        throw new RuntimeException("ajaxExceptionTest");
    }

    @RequestMapping("test")
    @ResponseBody
    public Result test(){
        SettleCompanyCheckDetailBizParam param=new SettleCompanyCheckDetailBizParam();
        return Result.wrapSuccessfulResult(settleCompanyCheckDetailBiz.insuranceCompanyCheckForERP(param));
    }


    @RequestMapping("testLock")
    @ResponseBody
    public Result testLock(){
       String key = "test123";
        redisLockClient.lock(key, new LockCallBack() {
            @Override
            public Object onTask() {
                return "1231";
            }
        });
        return Result.wrapSuccessfulResult(key);
    }

    @RequestMapping("testTransaction")
    @ResponseBody
    public PagingResult testTransaction(){
        ShopSettleDetailPackageModeQueryParam param = new ShopSettleDetailPackageModeQueryParam();
        param.setCooperationModeId(CooperationModeEnum.GIVE_INSURANCE.getCode());
        param.setPageSize(10);

        return rpcSettleShopService.shopSettleDetailPackageModePage(param);
    }

}
