package com.tqmall.mana.biz.manager.settle.settleCalculate;

import com.tqmall.mana.beans.BO.settle.calculate.InsuranceSettleBasicBO;

/**
 * Created by huangzhangting on 17/3/23.
 */
public interface InsuranceSettleCalculateExtendBiz {

    /**
     * 门店录入保单，审核通过后调用，生成结算数据
     * @param basicBO
     */
    void calculateSettleData(InsuranceSettleBasicBO basicBO);

}
