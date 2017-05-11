package com.tqmall.mana.biz.manager.outInsurance;


import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.outInsurance.PingAnBO;
import com.tqmall.mana.beans.entity.insurance.ManaInsuranceItemDO;

import java.util.Map;

/**
 * Created by zxg on 16/11/30.
 * 10:17
 * no bug,以后改代码的哥们，祝你好运~！！
 * 平安车险数据
 */
public interface PingAnInsuranceBiz {

    //  获得商业险数据,返回 anxinName+basicId:ManaInsuranceItemDO
    Result<Map<ManaInsuranceItemDO,ManaInsuranceItemDO>> getSYData(PingAnBO pingAnBO);
}
