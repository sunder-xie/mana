package com.tqmall.mana.dao.mapper.customer;

import com.tqmall.mana.beans.entity.customer.CustomerVehiclePageDO;
import com.tqmall.mana.beans.entity.customer.ManaCustomerVehicleDO;
import com.tqmall.mana.beans.param.CommonPageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManaCustomerVehicleDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManaCustomerVehicleDO record);

    ManaCustomerVehicleDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaCustomerVehicleDO record);

    List<ManaCustomerVehicleDO> selectListByDO(ManaCustomerVehicleDO record);

    /**
     * 分页查询客户车辆信息
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

}