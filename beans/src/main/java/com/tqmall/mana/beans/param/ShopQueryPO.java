package com.tqmall.mana.beans.param;

import lombok.Data;

/**
 *
 * 备注：暂时没有分页需求，后续有需求了自行扩展
 *
 * Created by huangzhangting on 17/2/9.
 */
@Data
public class ShopQueryPO {
    private Integer cooperationMode; //保险模式
    private String queryStr; //关键词（名店名称或者手机号）

}
