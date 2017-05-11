package com.tqmall.mana.beans.param.settle;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * 查询门店返利的时候，使用到
 *
 * Created by huangzhangting on 17/2/20.
 */
@Data
public class ShopSettleRateQueryParam {
    private Integer insuranceCompanyId; //保险公司id
    private Integer cooperationMode; //保险模式
    private Integer shopId; //门店id
    private String cityCode; //投保城市编码
    private Integer rebateStandard; //返点基准 1:交强险 2:商业险 3:服务包
    private BigDecimal insuredFee; //保费
}
