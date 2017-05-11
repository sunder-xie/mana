package com.tqmall.mana.dao.mapper.cashcoupon;

import com.tqmall.mana.beans.entity.cashcoupon.CashCouponRegionConfigDO;
import com.tqmall.mana.beans.entity.cashcoupon.CashCouponRuleConfigDO;

import java.util.List;
import java.util.Map;

public interface CashCouponRegionConfigDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CashCouponRegionConfigDO record);

    int insertSelective(CashCouponRegionConfigDO record);

    CashCouponRegionConfigDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CashCouponRegionConfigDO record);

    int updateByRuleConfigId(CashCouponRegionConfigDO record);

    int updateByPrimaryKey(CashCouponRegionConfigDO record);

    CashCouponRegionConfigDO selectByCityCode(String cityCode);

    List<String> selectCityCode();

    int selectRegionConfigCount(Map<String,Object> map);

    List<CashCouponRegionConfigDO> selectRegionConfigPageList(Map<String,Object> map);

    List<CashCouponRegionConfigDO> selectRegionConfigByRuleConfigId(Integer ruleConfigId);

}