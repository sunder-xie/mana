package com.tqmall.mana.beans.param.settle;

import lombok.Data;

import java.util.List;

/**
 * Created by huangzhangting on 17/1/17.
 */
@Data
public class SettleShopRulePageParam {
    private Integer insuranceCompanyId; //保险公司id
    private String provinceCode; //省份编码
    private String cityCode; //城市编码
    private Integer shopId; //门店id
    private List<Integer> cooperationModes; //保险模式集合

    /** 每页个数 */
    private Integer pageSize;
    /** 页码，从 1 开始 */
    private Integer pageIndex;
    /** 分页查询偏移量，数据库使用（前端不用管这个参数） */
    private Integer offerSet;

}
