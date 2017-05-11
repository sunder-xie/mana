package com.tqmall.mana.dao.mapper.customer;

import com.tqmall.mana.beans.entity.customer.ManaCustomerInfoReplaceLogDO;

public interface ManaCustomerInfoReplaceLogDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManaCustomerInfoReplaceLogDO record);

    ManaCustomerInfoReplaceLogDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaCustomerInfoReplaceLogDO record);


}