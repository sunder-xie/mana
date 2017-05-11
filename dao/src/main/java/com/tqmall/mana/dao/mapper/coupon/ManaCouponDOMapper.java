package com.tqmall.mana.dao.mapper.coupon;

import com.tqmall.mana.beans.BO.coupon.UpdateCouponBO;
import com.tqmall.mana.beans.entity.coupon.ManaCouponDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManaCouponDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManaCouponDO record);

    ManaCouponDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaCouponDO record);

    List<ManaCouponDO> selectAvailableList(@Param("typeId")Integer typeId, @Param("limitNum")Integer limitNum);

    int updateCouponStatus(UpdateCouponBO updateCouponBO);
}