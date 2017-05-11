package com.tqmall.mana.dao.mapper.customer;

import com.tqmall.mana.beans.entity.customer.ManaCustomerCommunicateRecordDO;
import com.tqmall.mana.beans.param.CommonPageParam;

import java.util.List;

public interface ManaCustomerCommunicateRecordDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManaCustomerCommunicateRecordDO record);

    ManaCustomerCommunicateRecordDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaCustomerCommunicateRecordDO record);

    /**
     * 通过车辆id获取客户最新沟通记录信息
     * @param customerVehicleId
     * * @return
     */
    ManaCustomerCommunicateRecordDO selectLatestCommunicateRecordDOById(Integer customerVehicleId);
    /**
     * 获取客户沟通记录分页数据
     * @param commonPageParam
     * @return
     */
    List<ManaCustomerCommunicateRecordDO> selectCommunicateRecordDOList(CommonPageParam commonPageParam);

    /**
     * 查询客户沟通记录总数
     * @param commonPageParam
     * @return
     */
    Integer selectCommunicateRecordDOCount(CommonPageParam commonPageParam);
}