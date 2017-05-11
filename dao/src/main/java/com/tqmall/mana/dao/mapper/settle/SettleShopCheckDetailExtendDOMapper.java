package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleInsuranceMaterialAllowanceDO;
import com.tqmall.mana.beans.entity.settle.SettleShopCheckDetailExtendDO;
import com.tqmall.mana.beans.entity.settle.extend.ShopSettleDetailExtendDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SettleShopCheckDetailExtendDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SettleShopCheckDetailExtendDO record);

    SettleShopCheckDetailExtendDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleShopCheckDetailExtendDO record);

    int updateDetailExtend(SettleInsuranceMaterialAllowanceDO record);


    SettleShopCheckDetailExtendDO selectByShopCheckDetailId(Integer shopCheckDetailId);


    List<ShopSettleDetailExtendDO> selectExtendDOByDetailIdList(@Param("detailIdList")List<Integer> detailIdList);

}