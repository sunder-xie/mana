package com.tqmall.mana.biz.manager.outInsurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.outInsurance.RenBaoBO;
import com.tqmall.mana.beans.entity.insurance.ManaInsuranceItemDO;

import java.util.Map;

/**
 * Created by zxg on 16/12/7.
 * 16:08
 * no bug,以后改代码的哥们，祝你好运~！！
 * 人保车险数据
 */
public interface RenBaoInsuranceBiz {

    //  获得商业险数据,返回 anxinName+basicId:ManaInsuranceItemDO
    Result<Map<ManaInsuranceItemDO,ManaInsuranceItemDO>> getSYData(RenBaoBO renBaoBO);
}
