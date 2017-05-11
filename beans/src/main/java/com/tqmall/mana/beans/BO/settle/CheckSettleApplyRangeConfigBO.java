package com.tqmall.mana.beans.BO.settle;

import lombok.Data;

import java.util.List;

/**
 *
 * 校验地区或者门店是否已经配置过结算规则
 *
 * Created by huangzhangting on 17/1/16.
 */
@Data
public class CheckSettleApplyRangeConfigBO {
    private Integer insuranceCompanyId;
    private Integer cooperationMode;
    private Integer shopId;
    private List<String> cityCodeList;
}
