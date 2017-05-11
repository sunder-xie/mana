package com.tqmall.mana.server;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.outInsurance.InsuranceCompareBO;
import com.tqmall.mana.beans.BO.outInsurance.InsuranceItemBO;
import com.tqmall.mana.biz.manager.insurance.InsuranceCompareBiz;
import com.tqmall.mana.client.beans.otherInsurance.InsuranceCompareDTO;
import com.tqmall.mana.client.service.InsuranceCompareService;
import com.tqmall.mana.component.util.BdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by zxg on 16/12/3.
 * 16:48
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Slf4j
public class InsuranceCompareServiceImpl implements InsuranceCompareService {

    @Autowired
    private InsuranceCompareBiz insuranceCompareBiz;

    @Override
    public Result<String> otherInsuranceCompare(InsuranceCompareDTO insuranceCompareDTO) {
        InsuranceCompareBO compareBO = BdUtil.do2bo(insuranceCompareDTO, InsuranceCompareBO.class);
        List<InsuranceItemBO> amountList = BdUtil.do2bo4List(insuranceCompareDTO.getAmountList(), InsuranceItemBO.class);
        compareBO.setAmountList(amountList);

//        throw new NullPointerException();
        Result<String>  result = insuranceCompareBiz.otherInsuranceCompare(compareBO);
        return result;
    }
}
