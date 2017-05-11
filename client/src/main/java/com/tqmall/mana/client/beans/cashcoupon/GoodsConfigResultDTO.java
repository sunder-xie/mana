package com.tqmall.mana.client.beans.cashcoupon;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by zhanghong on 17/4/11.
 */
@Data
public class GoodsConfigResultDTO implements Serializable {

    /**送券商品类型,1:自营 2:非自营**/
    private Integer goodsType;

    /**商品金额折算系数**/
    private BigDecimal goodsCoefficient;


}
