package com.tqmall.mana.dao.mapper.customer;

import com.tqmall.mana.beans.entity.customer.ManaCustomerPreferentialLogDO;
import com.tqmall.mana.beans.param.PreferentialPageParam;

import java.util.List;

public interface ManaCustomerPreferentialLogDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManaCustomerPreferentialLogDO record);



    ManaCustomerPreferentialLogDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaCustomerPreferentialLogDO record);

    List<ManaCustomerPreferentialLogDO> getPreferentialDOList(Integer vehicleId);

    /**
     *  通过分页参数获取优惠分页列表
     * @param preferentialPageParam
     * @return
     */
    List<ManaCustomerPreferentialLogDO> getPreferentialPagingList(PreferentialPageParam preferentialPageParam);

    Integer getPreferentialPagingCount(PreferentialPageParam preferentialPageParam);
}