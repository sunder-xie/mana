package com.tqmall.mana.biz.manager.settle.config;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.mana.beans.BO.settle.SettleRateBO;
import com.tqmall.mana.beans.VO.settle.shopRule.ModifyShopRuleVO;
import com.tqmall.mana.beans.VO.settle.shopRule.SettleShopRuleVO;
import com.tqmall.mana.beans.VO.settle.shopRule.add.AddSettleShopRuleVO;
import com.tqmall.mana.beans.entity.settle.SettleShopRuleItemDO;
import com.tqmall.mana.beans.param.settle.SettleShopRulePageParam;
import com.tqmall.mana.beans.param.settle.ShopSettleRateQueryParam;

import java.util.Date;
import java.util.List;

/**
 * 门店结算规则配置业务代码
 * <p>
 * Created by huangzhangting on 17/1/13.
 */
public interface SettleShopRuleBiz {
    /**
     * 新增规则，通用
     */
    boolean addSettleShopRule(AddSettleShopRuleVO shopRuleVO);

    /**
     * 新增规则，奖励金模式
     */
    boolean addSettleShopRuleForReward(AddSettleShopRuleVO shopRuleVO);

    /**
     * 查询组配置是否在使用中true:使用中,false:不在使用中
     *
     * @param basicId
     * @return
     */
    boolean querySettleConfigIsUse(Integer basicId);

    /**
     * 分页查询门店结算规则数据
     * @param param
     * @return
     */
    PagingResult<SettleShopRuleVO> queryShopRulePage(SettleShopRulePageParam param);

    /**
     * 删除规则配置
     * @param id
     * @return
     */
    boolean deleteShopRule(Integer id);

    /**
     * 修改规则，通用
     * @param modifyShopRuleVO
     * @return
     */
    boolean modifyShopRule(ModifyShopRuleVO modifyShopRuleVO);

    /**
     * 修改规则，奖励金模式
     * @param modifyShopRuleVO
     * @return
     */
    boolean modifyShopRuleForReward(ModifyShopRuleVO modifyShopRuleVO);

    /*====== 获得 ruleItem 列表 =======*/

    /**
     * h获得门店结算规则明细列表
     * @param calculateMode 计算方式，不能为null
     * @param searchTime 规则时间，不能为null,规则有效时间开始 < searchTime < 规则有效日期结束
     * @return
     */
    List<SettleShopRuleItemDO> getRuleItemList(Integer calculateMode,Date searchTime);


    /**
     * 结算比例查询
     * @param param
     * @return
     */
    SettleRateBO getSettleRate(ShopSettleRateQueryParam param);

    /**
     * 根据id查询配置规则
     * @param id
     * @return
     */
    SettleShopRuleVO getSettleShopRule(Integer id);


    /**
     * 通过合作模式查询shop_rule_id list
     * @param cooperationMode
     * @return
     */
    List<Integer> getRuleIdListByCooperationMode(Integer cooperationMode,Integer insuranceCompanyId);
}
