package com.tqmall.mana.test.biz.goods;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.goods.GoodsBasicBO;
import com.tqmall.mana.biz.manager.goods.GoodsConvertBiz;
import com.tqmall.mana.component.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by zhouheng on 17/3/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
@Slf4j
public class GoodsConvertTest {

    @Autowired
    private GoodsConvertBiz goodsConvertBiz;

    @Test
    public void goodsConvertTest(){

        Result<List<GoodsBasicBO>> result = goodsConvertBiz.getGoodsConvertList("101");
        log.info("************goodsConvertTest*************", JsonUtil.objectToStr(result));

    }

}
