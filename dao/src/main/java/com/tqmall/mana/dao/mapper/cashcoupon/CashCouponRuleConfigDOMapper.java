package com.tqmall.mana.dao.mapper.cashcoupon;

import com.tqmall.mana.beans.entity.cashcoupon.CashCouponRuleConfigDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface CashCouponRuleConfigDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CashCouponRuleConfigDO record);

    int insertSelective(CashCouponRuleConfigDO record);

    CashCouponRuleConfigDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CashCouponRuleConfigDO record);

    int updateByPrimaryKey(CashCouponRuleConfigDO record);

    List<CashCouponRuleConfigDO> batchSelectConfigList(@Param("ruleIds") Set<Integer> ruleIds);
}