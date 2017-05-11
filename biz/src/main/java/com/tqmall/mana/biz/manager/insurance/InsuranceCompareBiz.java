package com.tqmall.mana.biz.manager.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.outInsurance.InsuranceCompareBO;

/**
 * Created by zxg on 16/12/3.
 * 14:32
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public interface InsuranceCompareBiz {

    /**
     * 去获得其他车险的比价结果，存储。返回是否成功
     * @param compareBO 必要参数
     * @return
     */
    Result<String>  otherInsuranceCompare(InsuranceCompareBO compareBO);
}
