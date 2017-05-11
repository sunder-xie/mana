package com.tqmall.mana.web.common.task;

import com.tqmall.mana.biz.manager.settle.config.SettleShopRuleBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zxg on 17/2/7.
 * 09:36
 * no bug,以后改代码的哥们，祝你好运~！！
 * 门店月结task
 */

@RestController
@Slf4j
public class ShopMonthSettledTask {

    @Autowired
    private SettleShopRuleBiz settleShopRuleBiz;
    // test
//    @Scheduled(cron = "30 * * * * ?")
//    public void test(){
//        System.out.println("test task========:========"+new Date().toString());
//    }

    // 暂时月累计不做每个自然月的1号的1点进行定时任务
//    @Scheduled(cron = "0 0 1 1 * ?")
//    public void mainTask() {
//        // 上个月的最后一天的最后一秒
//
//    }


}
