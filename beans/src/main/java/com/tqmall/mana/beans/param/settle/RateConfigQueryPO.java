package com.tqmall.mana.beans.param.settle;

import lombok.Data;

import java.util.Set;

/**
 * Created by huangzhangting on 17/3/22.
 */
@Data
public class RateConfigQueryPO {
    //数据id集合
    private Set<Integer> ids;
    //保险公司id
    private Integer insuranceCompanyId;
    //城市编码
    private String cityCode;
    //适用范围
    private Integer applyRange;
}
