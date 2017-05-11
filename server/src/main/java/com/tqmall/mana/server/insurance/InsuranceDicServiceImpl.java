package com.tqmall.mana.server.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.insurance.InsuranceDicBO;
import com.tqmall.mana.biz.manager.insurance.InsuranceDicBiz;
import com.tqmall.mana.client.beans.insurance.InsuranceDicDTO;
import com.tqmall.mana.client.service.insurance.InsuranceDicService;
import com.tqmall.mana.component.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by zxg on 17/1/17.
 * 20:17
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class InsuranceDicServiceImpl implements InsuranceDicService {
    @Autowired
    private InsuranceDicBiz insuranceDicBiz;


    @Override
    public Result<List<InsuranceDicDTO>> getCooperationModeList() {
        List<InsuranceDicBO> list = insuranceDicBiz.getCooperationModeList();
        return ResultUtil.successListResult(list,InsuranceDicDTO.class);
    }

    @Override
    public Result<List<InsuranceDicDTO>> getSettleProjectList() {
        List<InsuranceDicBO> list = insuranceDicBiz.getSettleProjectList();
        return ResultUtil.successListResult(list,InsuranceDicDTO.class);
    }

    @Override
    public Result<List<InsuranceDicDTO>> getInsuranceTypeList() {
        List<InsuranceDicBO> list = insuranceDicBiz.getInsuranceTypeList();
        return ResultUtil.successListResult(list, InsuranceDicDTO.class);
    }

    @Override
    public Result<List<InsuranceDicDTO>> getSettleStatusList() {
        List<InsuranceDicBO> list = insuranceDicBiz.getSettleStatusList();
        return ResultUtil.successListResult(list, InsuranceDicDTO.class);
    }

    @Override
    public Result<List<InsuranceDicDTO>> getScenarioTypeList() {
        List<InsuranceDicBO> list = insuranceDicBiz.getScenarioTypeList();
        return ResultUtil.successListResult(list, InsuranceDicDTO.class);
    }

    @Override
    public Result<List<InsuranceDicDTO>> getConfirmMoneyStatusList() {
        List<InsuranceDicBO> list = insuranceDicBiz.getConfirmMoneyStatusList();
        return ResultUtil.successListResult(list, InsuranceDicDTO.class);
    }

    @Override
    public Result<List<InsuranceDicDTO>> getRewardStatusList() {
        List<InsuranceDicBO> list = insuranceDicBiz.getRewardStatusList();
        return ResultUtil.successListResult(list, InsuranceDicDTO.class);
    }

    @Override
    public Result<List<InsuranceDicDTO>> getWithdrawCashStatusList() {
        List<InsuranceDicBO> list = insuranceDicBiz.getWithdrawCashStatusList();
        return ResultUtil.successListResult(list, InsuranceDicDTO.class);
    }

    @Override
    public Result<List<InsuranceDicDTO>> getBalanceStatusList() {
        List<InsuranceDicBO> list = insuranceDicBiz.getBalanceStatusList();
        return ResultUtil.successListResult(list, InsuranceDicDTO.class);
    }

}
