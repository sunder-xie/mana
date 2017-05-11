package com.tqmall.mana.beans.param.settle;

import lombok.Data;

/**
 * Created by zhouheng on 17/3/13.
 */
@Data
public class SettleShopRuleRegionParam {

    /**
     *
     */
    private Integer cooperationMode;
    private Integer insuranceCompanyId;
    private String regionParentCode;
    private Integer regionLevel;
    private Integer settleRuleId;

}
