package com.tqmall.mana.dao.mapper.customer;

import com.tqmall.mana.beans.entity.customer.CustomerPageDO;
import com.tqmall.mana.beans.entity.customer.CustomerVehiclePageDO;
import com.tqmall.mana.beans.entity.customer.ManaCustomerDO;
import com.tqmall.mana.beans.param.CommonPageParam;

import java.util.List;

public interface ManaCustomerDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManaCustomerDO record);

    ManaCustomerDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaCustomerDO record);

    /**
     * 查询客户信息
     * @param record
     * @return
     */
    List<ManaCustomerDO> selectListByDO(ManaCustomerDO record);

    /**
     * 分页查询客户信息
     * @param commonPageParam
     * @return
     */
    List<CustomerVehiclePageDO> selectPagingList(CommonPageParam commonPageParam);

    /**
     * 查询分页总数
     * @param commonPageParam
     * @return
     */
    Integer selectCountByDO(CommonPageParam commonPageParam);

    /**
     * 查询客户信息分页
     *
     * @param commonPageParam
     * @return
     */
    List<CustomerPageDO> selectCustomerFormPageList(CommonPageParam commonPageParam);

    /**
     * 查询客户信息分页总数
     * @param commonPageParam
     * @return
     */
    Integer selectCustomerFormPageCount(CommonPageParam commonPageParam);
}