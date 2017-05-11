package com.tqmall.mana.biz.manager.insurance;

import com.tqmall.mana.beans.BO.insurance.InsuranceDicBO;

import java.util.List;

/**
 * Created by zxg on 17/1/17.
 * 20:22
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public interface InsuranceDicBiz {
    /*模式，例如模式一 买保险送服务包*/
    List<InsuranceDicBO> getCooperationModeList();
    String getCooperationModeNameByDicId(Integer dicId);

    /*结算项目，例如 商业险返利*/
    List<InsuranceDicBO> getSettleProjectList();
    String getSettleProjectNameByDicId(Integer dicId);

    /*适用范围 例如-1:地区 2:门店*/
    List<InsuranceDicBO> getApplyRangeList();
    String getApplyRangeNameByDicId(Integer dicId);

    /*计算方式 例如-1:单笔 2:月累计*/
    List<InsuranceDicBO> getCalculateModeList();
    String getCalculateModeNameByDicId(Integer dicId);

    /*资金类型 例如-1:现金 2:奖励金*/
    List<InsuranceDicBO> getFundTypeList();
    String getFundTypeNameByDicId(Integer dicId);

    /*结算条件 例如-1:签单日期 2:起保日期 3:服务包支付日期
	签单日期：保单缴费成功时间
	*/
    List<InsuranceDicBO> getSettleConditionList();
    String getSettleConditionNameByDicId(Integer dicId);

    /*返点基准 例如-1:交强险 2:商业险 3:服务包*/
    List<InsuranceDicBO> getRebateStandardList();
    String getRebateStandardNameByDicId(Integer dicId);

    /*返点类型 例如-1:比例 2:服务包工时费*/
    List<InsuranceDicBO> getRebateTypeList();
    String getRebateTypeNameByDicId(Integer dicId);

    /*投保场景*/
    List<InsuranceDicBO> getScenarioTypeList();
    String getScenarioTypeNameByDicId(Integer dicId);

    /*结算规则类型 例如-1:现金 2:奖励金 3:服务包*/
    List<InsuranceDicBO> geSettleRuleTypeList();
    String getSettleRuleTypeNameByDicId(Integer dicId);

    /* 结算状态 */
    List<InsuranceDicBO> getSettleStatusList();
    String getSettleStatusNameByDicId(Integer dicId);

    /* 险种 */
    List<InsuranceDicBO> getInsuranceTypeList();
    String getInsuranceTypeNameByDicId(Integer dicId);

    /* 收款状态 */
    List<InsuranceDicBO> getConfirmMoneyStatusList();
    String getConfirmMoneyStatusNameByDicId(Integer dicId);

    /* 奖励金状态 */
    List<InsuranceDicBO> getRewardStatusList();
    String getRewardStatusNameByDicId(Integer dicId);

    /* 奖励金提现状态 */
    List<InsuranceDicBO> getWithdrawCashStatusList();
    String getWithdrawCashStatusNameByDicId(Integer dicId);

    /* 对账状态 */
    List<InsuranceDicBO> getBalanceStatusList();
    String getBalanceStatusNameByDicId(Integer dicId);

}
