package com.tqmall.mana.dao.mapper.customer;

import com.tqmall.mana.beans.entity.customer.ManaCustomerAddressDO;

import java.util.List;

public interface ManaCustomerAddressDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManaCustomerAddressDO record);

    ManaCustomerAddressDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaCustomerAddressDO record);

    /**
     * 通过客户id查询客户地址信息
     * @param customerId
     * @return
     */
    List<ManaCustomerAddressDO> selectAddressListById(Integer customerId);
}