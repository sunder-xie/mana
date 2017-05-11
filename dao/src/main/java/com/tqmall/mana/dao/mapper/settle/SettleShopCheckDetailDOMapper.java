package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleInsuranceMaterialAllowanceDO;
import com.tqmall.mana.beans.entity.settle.SettleShopCheckDetailDO;
import com.tqmall.mana.beans.entity.settle.extend.ShopSettleDetailForCashBackDO;
import com.tqmall.mana.beans.param.settle.SettleShopCheckDetailQueryPO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SettleShopCheckDetailDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SettleShopCheckDetailDO record);

    SettleShopCheckDetailDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleShopCheckDetailDO record);

    int updateDetail(SettleInsuranceMaterialAllowanceDO record);

    List<SettleShopCheckDetailDO> selectByIdList(@Param("idList")List<Integer> idList);

    List<SettleShopCheckDetailDO> selectByCondition(SettleShopCheckDetailQueryPO queryPO);

    /**
     * 淘汽确认收到保险公司返利，查询必要信息，通知finance奖励金生效
     * @param queryPO
     * @return
     */
    List<ShopSettleDetailForCashBackDO> selectForCashBack(SettleShopCheckDetailQueryPO queryPO);

    /**
     *
     * erp-查询保险机滤门店名称列表
     *
     **/
    List<SettleShopCheckDetailDO> selectAgentNameList(@Param("shopName") String shopName);

    /**
     * 根据orderSnList查询
     * @param orderSn
     * @return
     */
    List<SettleInsuranceMaterialAllowanceDO> selectByOrderSnList(@Param("orderSnList") List<String> orderSn);


    /**
     * erp-查询保险机滤请款列表总数
     * @param map
     * @return
     */
    Integer checkAllowanceListCount ( Map<String,Object> map );

    /**
     * erp-查询保险机滤请款总额
     * @param map
     * @return
     */
    BigDecimal checkPayableAmountCount (Map<String,Object> map );

    /**
     *
     * erp-查询保险机滤请款列表
     *
     **/
    List<SettleInsuranceMaterialAllowanceDO> checkAllowanceList (Map<String,Object> map );


    List<SettleShopCheckDetailDO> getDeletedData(@Param("insuranceOrderSnList")List<String> insuranceOrderSnList);
}