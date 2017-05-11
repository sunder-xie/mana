package com.tqmall.mana.biz.manager.settle.settleCalculate;

import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettleBasicMsg;
import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettleMaterialAllowanceMsg;

/**
 *
 * 保险结算数据计算业务代码
 *
 * Created by huangzhangting on 17/1/22.
 */
public interface InsuranceSettleCalculateBiz {

    void lockCalculateSettleData(InsuranceSettleBasicMsg basicMsg);

    /**
     * 计算结算数据
     * @param basicMsg
     * @param isMqInvoke true:消息触发 false:手动触发
     */
    void calculateSettleData(InsuranceSettleBasicMsg basicMsg, boolean isMqInvoke);

    /**
     * 物料补贴结算数据
     * @param materialAllowanceMsg
     */
    void calculateSettleDataForMaterialAllowance(InsuranceSettleMaterialAllowanceMsg materialAllowanceMsg);

}
