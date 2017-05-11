package com.tqmall.mana.beans.param.settle;

import lombok.Data;

import java.util.List;

/**
 * Created by huangzhangting on 17/2/6.
 */
@Data
public class SettleShopCheckDetailQueryPO {
    private Integer settleProjectId; //结算项目id
    private Integer settleRuleType; //对账类型：1:现金 2:奖励金 3:服务包
    private List<Integer> idList;
    private List<String> insuranceOrderSnList;
    private List<String> bizSnList;
    private Integer agentId;
    private Integer insuranceCompanyId;
}
