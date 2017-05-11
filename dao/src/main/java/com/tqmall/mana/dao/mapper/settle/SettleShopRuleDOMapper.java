package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleShopRuleDO;
import com.tqmall.mana.beans.param.settle.SettleShopRulePageParam;
import com.tqmall.mana.beans.param.settle.ShopSettleRateQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SettleShopRuleDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SettleShopRuleDO record);

    SettleShopRuleDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleShopRuleDO record);


    /** 仅查询 settle_shop_rule */
    List<SettleShopRuleDO> selectShopRulePage(SettleShopRulePageParam pageParam);
    int selectShopRuleCount(SettleShopRulePageParam pageParam);


    /** 关联地区配置表查询 */
    List<SettleShopRuleDO> selectShopRuleForRegionPage(SettleShopRulePageParam pageParam);
    int selectShopRuleForRegionCount(SettleShopRulePageParam pageParam);


    /** 关联门店配置表查询 */
    List<SettleShopRuleDO> selectShopRuleForShopPage(SettleShopRulePageParam pageParam);
    int selectShopRuleForShopCount(SettleShopRulePageParam pageParam);


    Integer selectIdWithShopConfig(ShopSettleRateQueryParam param);

    Integer selectIdWithRegionConfig(ShopSettleRateQueryParam param);

    List<Integer> selectRuleIdListByCooperationMode(@Param("cooperationMode") Integer cooperationMode,@Param("companyId")Integer companyId);

}