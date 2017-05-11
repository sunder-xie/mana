package com.tqmall.mana.biz.manager.settle.settleCalculate;

import com.tqmall.mana.beans.BO.settle.calculate.InsuranceSettleBasicBO;
import com.tqmall.mana.beans.BO.settle.calculate.InsuranceSettleFormBO;
import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettleBasicMsg;
import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettleFormMsg;
import com.tqmall.mana.component.annotation.lock.ManaLock;
import com.tqmall.mana.component.enums.insurance.dict.CooperationModeEnum;
import com.tqmall.mana.component.redis.RedisKeyBean;
import com.tqmall.mana.component.util.BdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhangting on 17/3/23.
 */
@Slf4j
@Service
public class InsuranceSettleCalculateExtendBizImpl implements InsuranceSettleCalculateExtendBiz {
    @Autowired
    private InsuranceSettleCalculateBiz settleCalculateBiz;


    private void checkBasicParam(InsuranceSettleBasicBO basicBO){
        Assert.notNull(basicBO, "参数不能为空");
        Integer cooperationModeId = basicBO.getCooperationModeId();
        Assert.notNull(CooperationModeEnum.codeDescription(cooperationModeId), "非法的保险模式："+cooperationModeId);
        Assert.notNull(basicBO.getInsuranceCompanyId(), "保险公司id不能为空");
        Assert.hasLength(basicBO.getInsuranceOrderSn(), "淘汽保单号不能为空");

        checkForm(basicBO.getFormBOList());
    }
    private void checkForm(List<InsuranceSettleFormBO> formBOList){
        Assert.notEmpty(formBOList, "保单不能为空");
        for(InsuranceSettleFormBO formBO : formBOList){
            Assert.hasLength(formBO.getInsuredFormNo(), "保单号不能为空");
            Assert.notNull(formBO.getInsuredFee(), "保费不能为空");
        }
    }

    @ManaLock(lockKeyName = RedisKeyBean.LOCK_CALCULATE_SETTLE_DATA_EXTEND)
    @Override
    public void calculateSettleData(InsuranceSettleBasicBO basicBO) {
        checkBasicParam(basicBO);

        InsuranceSettleBasicMsg basicMsg = BdUtil.do2bo(basicBO, InsuranceSettleBasicMsg.class);
        List<InsuranceSettleFormMsg> formMsgList = new ArrayList<>();
        for(InsuranceSettleFormBO formBO : basicBO.getFormBOList()){
            InsuranceSettleFormMsg formMsg = BdUtil.do2bo(formBO, InsuranceSettleFormMsg.class);
            formMsg.setCooperationModeId(basicMsg.getCooperationModeId());
            formMsgList.add(formMsg);
        }
        basicMsg.setFormMsgList(formMsgList);

        settleCalculateBiz.calculateSettleData(basicMsg, false);
    }

}
