package com.tqmall.mana.beans.BO.goods;

import lombok.Data;

/**
 * Created by zhouheng on 17/3/11.
 */
@Data
public class GoodsBasicBO {

    /**
     * 商品id
     */
    private Integer id;
    /**
     * 商品编码
     */
    private String goodsSn;
    /**
     * 商品名称
     */
    private String goodsName;

}
