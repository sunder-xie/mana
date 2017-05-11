package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.BO.settle.SettleCarOwnerAmountSumBO;
import com.tqmall.mana.beans.entity.settle.SettleCarOwnerCheckDetailDO;
import com.tqmall.mana.beans.param.settle.SettleCarOwnerCheckerForBrilliantBizParam;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SettleCarOwnerCheckDetailDOMapper {
    int deleteByPrimaryKey(Integer id);


    int insertSelective(SettleCarOwnerCheckDetailDO record);

    SettleCarOwnerCheckDetailDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleCarOwnerCheckDetailDO record);


    List<SettleCarOwnerCheckDetailDO> getCarOwnerCheckDetailForPageByInsuranceOrderSnList(@Param("insuranceOrderSnList") List<String> insuranceOrderSnList);

    Integer getCountByCondation(Map<String, Object> map);

    SettleCarOwnerAmountSumBO getInsuranceFeeTotalAmount(Map<String, Object> map);

    Integer updateConfirmMoneyStatusByIds(Map<String, Object> map);

    Integer updatePayStatusById(Map<String, Object> map);

    SettleCarOwnerCheckDetailDO selectByPackageOrderSn(String packageOrderSn);

    int updateByInsuranceOrderSn(SettleCarOwnerCheckerForBrilliantBizParam param);

    List<String> getInsuranceOrderSnForPage(Map<String, Object> map);

    List<SettleCarOwnerCheckDetailDO> getCarOwnerCheckDetailDOByIdListAndTqmallGetPaidStatus(@Param("idList") List<Integer> idList, @Param("status") Integer status);

    SettleCarOwnerCheckDetailDO getCarOwnerCheckerDetailDOByIdAndTqmallPayStatus(@Param("id") Integer id, @Param("payStatus") Integer payStatus);


    List<SettleCarOwnerCheckDetailDO> getDeletedData(@Param("insuranceOrderSnList")List<String> insuranceOrderSnList);
}