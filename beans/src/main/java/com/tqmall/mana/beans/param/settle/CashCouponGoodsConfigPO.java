package com.tqmall.mana.beans.param.settle;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhanghong on 17/4/10.
 */
@Data
public class CashCouponGoodsConfigPO {

    /**送券商品类型,1:自营 2:非自营**/
    private Integer goodsType;

    /**商品金额折算系数**/
    private BigDecimal goodsCoefficient;
}
