package com.tqmall.mana.client.service.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.client.beans.insurance.InsuranceDicDTO;

import java.util.List;

/**
 * Created by zxg on 17/1/17.
 * 20:11
 * no bug,以后改代码的哥们，祝你好运~！！
 * 保险项目的所有字典数据
 */
public interface InsuranceDicService {

    // 获得模式列表，例如模式一 买保险送奖励金
    Result<List<InsuranceDicDTO>> getCooperationModeList();

    // 获得结算项目列表，例如：商业险返利
    Result<List<InsuranceDicDTO>> getSettleProjectList();

    //获取险种列表
    Result<List<InsuranceDicDTO>> getInsuranceTypeList();

    //获取结算状态（支付状态）列表
    Result<List<InsuranceDicDTO>> getSettleStatusList();

    //获取投保场景列表
    Result<List<InsuranceDicDTO>> getScenarioTypeList();

    //获取收款状态列表
    Result<List<InsuranceDicDTO>> getConfirmMoneyStatusList();

    //获取奖励金状态列表
    Result<List<InsuranceDicDTO>> getRewardStatusList();

    //获取奖励金提现状态列表
    Result<List<InsuranceDicDTO>> getWithdrawCashStatusList();

    //获取对账状态列表
    Result<List<InsuranceDicDTO>> getBalanceStatusList();

}
