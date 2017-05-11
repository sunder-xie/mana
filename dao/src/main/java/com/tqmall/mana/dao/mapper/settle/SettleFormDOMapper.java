package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.BO.settle.SettleCompanyAmountSumBO;
import com.tqmall.mana.beans.BO.settle.SettleFormBO;
import com.tqmall.mana.beans.entity.settle.SettleFormDO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SettleFormDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SettleFormDO record);

    SettleFormDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleFormDO record);

    Integer getCountByCondition(Map<String, Object> map);

    List<SettleFormBO> getSettleFormListForPage(Map<String, Object> map);

    SettleCompanyAmountSumBO sumInsuredFee(Map<String, Object> map);

    List<SettleFormDO> getSettleFormListByInsuranceOrderSn(@Param("insuranceOrderSnList") List<String> insuranceOrderSnList);

    Integer selectIdByFormNo(@Param("formNo")String formNo, @Param("insuranceCompanyId")Integer insuranceCompanyId);

    //BigDecimal getPayableTotalAmount(Map<String,Object> map);

//    SettleFormDO selectByFormNo(String formNo);


    List<SettleFormDO> selectByAgentId(Integer agentId);

    List<SettleFormDO> getDeletedData(@Param("insuranceOrderSnList")List<String> insuranceOrderSnList);
}