package com.tqmall.mana.biz.manager.settle.config;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.settle.SettleShopRuleIntroductionBO;

import java.util.List;

/**
 * Created by zhouheng on 17/2/7.
 */
public interface SettleShopRuleIntroductionBiz {

    /**
     * 新增结算规则说明
     *
     * @param introductionBO
     * @return
     */
    Result addRuleIntroduction(SettleShopRuleIntroductionBO introductionBO);

    /**
     * 删除结算规则说明
     *
     * @param introductionId
     * @return
     */
    Result deleteIntroduction(Integer introductionId);

    /**
     * 更新结算规则说明
     *
     * @param introductionBO
     * @return
     */
    Result updateIntroduction(SettleShopRuleIntroductionBO introductionBO);

    /**
     * 查询结算规则说明
     *
     * @param introductionId
     * @return
     */
    Result<SettleShopRuleIntroductionBO> getRuleIntroductionInfo(Integer introductionId);

    /**
     * 获取三种结算规则列表
     *
     * @return
     */
    Result<List<SettleShopRuleIntroductionBO>> getRuleIntroductionList();

    /**
     * 通过规则类型查询规则说明信息
     *
     * @param ruleType
     * @return
     */
    Result<SettleShopRuleIntroductionBO> getRuleIntroductionByRuleType(Integer ruleType);

}
