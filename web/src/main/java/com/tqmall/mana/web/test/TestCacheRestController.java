package com.tqmall.mana.web.test;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.biz.manager.insurance.InsuranceDicBiz;
import com.tqmall.mana.biz.manager.test.TestCacheBiz;
import com.tqmall.mana.biz.manager.test.TestCacheStaticBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zxg on 17/1/10.
 * 23:21
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@RestController
@RequestMapping("testCache")
public class TestCacheRestController {
    @Autowired
    private TestCacheBiz testCacheBiz;
    @Autowired
//    @Qualifier("insuranceDicBizImpl")
    private InsuranceDicBiz dicBiz;

    @RequestMapping("testString")
    public Result testString(){
        return Result.wrapSuccessfulResult(testCacheBiz.testString("a",10));
    }

    @RequestMapping("testList")
    public Result testList(){
        return Result.wrapSuccessfulResult(testCacheBiz.testList(20));
    }


    @RequestMapping("testStatic")
    public Result testStatic(){
        return Result.wrapSuccessfulResult(TestCacheStaticBiz.getResult());
    }
    @RequestMapping("dicBiz")
    public Result dicBiz(){
        return Result.wrapSuccessfulResult(dicBiz.getCalculateModeList());
    }
}
