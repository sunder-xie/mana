package com.tqmall.mana.biz.manager.insurance;


import com.tqmall.mana.beans.VO.settle.AddMaterialAllowanceVO;

/**
 *
 * 保险结算数据手动同步
 *
 * Created by huangzhangting on 17/3/15.
 */
public interface InsuranceSettleSyncBiz {
    /**
     * 根据车牌号同步结算数据
     * @param vehicleSn
     */
    void syncDataByVehicleSn(String vehicleSn);

    /**
     * 添加机滤补贴
     */
    void addMaterialAllowance(AddMaterialAllowanceVO allowanceVO);

}
