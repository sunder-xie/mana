package com.tqmall.mana.client.service;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.client.beans.otherInsurance.InsuranceCompareDTO;

/**
 * Created by zxg on 16/12/3.
 * 14:32
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public interface InsuranceCompareService {

    /**
     * 去获得其他车险的比价结果，存储。返回是否成功
     * @param insuranceCompareDTO 必要参数
     * @return
     */
    Result<String>  otherInsuranceCompare(InsuranceCompareDTO insuranceCompareDTO);
}
