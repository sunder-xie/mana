package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleCompanyCheckDetailDO;
import com.tqmall.mana.beans.entity.settle.extend.InsuranceCompanySettleDetailDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SettleCompanyCheckDetailDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SettleCompanyCheckDetailDO record);

    SettleCompanyCheckDetailDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleCompanyCheckDetailDO record);

    int batchInsert(@Param("list") List<SettleCompanyCheckDetailDO> list);

    /**
     * 确认收款数据查询
     *
     * @param idList
     * @return
     */
    List<InsuranceCompanySettleDetailDO> selectByIdsForConfirmMoney(@Param("idList") List<Integer> idList);

    List<SettleCompanyCheckDetailDO> selectBySettleFormSnList(@Param("settleFormSnList") List<String> settleFormSnList,
                                                              @Param("insuranceCompanyId")Integer insuranceCompanyId);


    List<SettleCompanyCheckDetailDO> getDeletedData(@Param("settleFormSnList") List<String> settleFormSnList,
                                                    @Param("insuranceCompanyId")Integer insuranceCompanyId);
}