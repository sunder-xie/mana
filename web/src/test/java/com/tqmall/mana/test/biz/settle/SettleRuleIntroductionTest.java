package com.tqmall.mana.test.biz.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.settle.SettleShopRuleIntroductionBO;
import com.tqmall.mana.biz.manager.settle.config.SettleShopRuleIntroductionBiz;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.test.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by zhouheng on 17/2/7.
 */
@Slf4j
public class SettleRuleIntroductionTest extends BaseTest {

    @Autowired
    private SettleShopRuleIntroductionBiz introductionBiz;

    @Test
    public void addRuleIntroductionTest(){
        SettleShopRuleIntroductionBO introductionBO = new SettleShopRuleIntroductionBO();
        introductionBO.setRuleIntroduction("<p4>你好啊XXXX</p4>");
        introductionBO.setRuleType(3);
        Result result = introductionBiz.addRuleIntroduction(introductionBO);
        log.info("addRuleIntroduction*******************"+JsonUtil.objectToStr(result));
    }

    @Test
    public void deleteIntroductionTest(){
        Result result = introductionBiz.deleteIntroduction(1);
        log.info("deleteIntroduction*******************"+JsonUtil.objectToStr(result));
    }

    @Test
    public void updateIntroductionTest(){
        SettleShopRuleIntroductionBO introductionBO = new SettleShopRuleIntroductionBO();
        introductionBO.setId(2);
        introductionBO.setGmtModified(new Date());
        introductionBO.setRuleIntroduction("<p4>你好啊XXXX</p4>");
        introductionBO.setRuleType(2);
        Result result = introductionBiz.updateIntroduction(introductionBO);
        log.info("updateIntroduction*******************"+JsonUtil.objectToStr(result));
    }

    @Test
    public void getRuleIntroductionInfoTest(){

        Result<SettleShopRuleIntroductionBO> result = introductionBiz.getRuleIntroductionInfo(2);
        log.info("getRuleIntroductionInfo*******************"+JsonUtil.objectToStr(result));

    }

    @Test
    public void getRuleIntroductionListTest(){

        Result<List<SettleShopRuleIntroductionBO>> result = introductionBiz.getRuleIntroductionList();
        log.info("getRuleIntroductionList*******************"+JsonUtil.objectToStr(result));

    }

}
