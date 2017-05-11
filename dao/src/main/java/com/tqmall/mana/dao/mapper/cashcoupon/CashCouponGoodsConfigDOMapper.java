package com.tqmall.mana.dao.mapper.cashcoupon;

import com.tqmall.mana.beans.entity.cashcoupon.CashCouponGoodsConfigDO;

import java.util.List;

public interface CashCouponGoodsConfigDOMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByRuleId(Integer ruleId);

    int insert(CashCouponGoodsConfigDO record);

    int insertSelective(CashCouponGoodsConfigDO record);

    CashCouponGoodsConfigDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CashCouponGoodsConfigDO record);

    int updateByPrimaryKey(CashCouponGoodsConfigDO record);

    int updateByRuleConfigId(CashCouponGoodsConfigDO record);

    List<CashCouponGoodsConfigDO> selectByRuleId(Integer ruleId);
}