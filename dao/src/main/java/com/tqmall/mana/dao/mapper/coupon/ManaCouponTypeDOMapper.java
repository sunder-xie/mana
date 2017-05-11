package com.tqmall.mana.dao.mapper.coupon;

import com.tqmall.mana.beans.entity.coupon.ManaCouponTypeDO;

import java.util.List;

public interface ManaCouponTypeDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManaCouponTypeDO record);

    ManaCouponTypeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaCouponTypeDO record);

    List<ManaCouponTypeDO> selectAll();

}