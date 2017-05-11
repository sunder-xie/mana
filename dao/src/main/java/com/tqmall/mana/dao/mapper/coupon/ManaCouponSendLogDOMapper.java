package com.tqmall.mana.dao.mapper.coupon;

import com.tqmall.mana.beans.BO.coupon.SearchCouponSendLogBO;
import com.tqmall.mana.beans.entity.coupon.ManaCouponSendLogDO;

import java.util.List;

public interface ManaCouponSendLogDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManaCouponSendLogDO record);

    ManaCouponSendLogDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaCouponSendLogDO record);

    int batchInsert(List<ManaCouponSendLogDO> list);

    List<ManaCouponSendLogDO> selectPage(SearchCouponSendLogBO searchCouponSendLogBO);

    int selectCount(SearchCouponSendLogBO searchCouponSendLogBO);

}