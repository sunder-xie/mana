package com.tqmall.mana.biz.manager.goods;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.goods.GoodsBasicBO;

import java.util.List;

/**
 * Created by zhouheng on 17/3/11.
 */
public interface GoodsConvertBiz {

    /**
     * 查询商品编码信息
     *
     * @param q
     * @return
     */
    Result<List<GoodsBasicBO>> getGoodsConvertList(String q);

}
