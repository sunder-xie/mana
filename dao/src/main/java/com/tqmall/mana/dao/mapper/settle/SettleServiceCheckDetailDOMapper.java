package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleServiceCheckDetailDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SettleServiceCheckDetailDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SettleServiceCheckDetailDO record);

    SettleServiceCheckDetailDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleServiceCheckDetailDO record);

    /**
     * 跟进订单编号查询
     * @param orderSnList
     * @return
     */
    List<SettleServiceCheckDetailDO> selectByOrderSnList(@Param("orderSnList")List<String> orderSnList);

    List<SettleServiceCheckDetailDO> selectBySearchDO(SettleServiceCheckDetailDO searchDO);

    /**
     * 根据淘汽保单号查询
     * @param orderSnList
     * @return
     */
    List<SettleServiceCheckDetailDO> selectByInsuranceOrderSnList(@Param("orderSnList")List<String> orderSnList);


    List<SettleServiceCheckDetailDO> getDeletedData(@Param("insuranceOrderSnList")List<String> insuranceOrderSnList);
}