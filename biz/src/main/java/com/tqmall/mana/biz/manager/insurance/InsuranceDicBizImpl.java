package com.tqmall.mana.biz.manager.insurance;

import com.google.common.collect.Lists;
import com.tqmall.mana.beans.BO.insurance.InsuranceDicBO;
import com.tqmall.mana.component.annotation.cache.ManaCache;
import com.tqmall.mana.component.enums.insurance.dict.*;
import com.tqmall.mana.component.redis.RedisKeyBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zxg on 17/1/18.
 * 09:30
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Service
@Slf4j
public class InsuranceDicBizImpl implements InsuranceDicBiz {

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_COOPERATION_MODE_KEY, cacheTime = 0)
    public List<InsuranceDicBO> getCooperationModeList() {
        return getEnumList(CooperationModeEnum.values());
    }

    @Override
    public String getCooperationModeNameByDicId(Integer dicId) {
        return getNameFromList(getCooperationModeList(), dicId);
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_SETTLE_PROJECT_KEY, cacheTime = 0)
    public List<InsuranceDicBO> getSettleProjectList() {
        return getEnumList(SettleProjectEnum.values());
    }

    @Override
    public String getSettleProjectNameByDicId(Integer dicId) {
        return getNameFromList(getSettleProjectList(), dicId);
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_APPLY_RANGE_KEY, cacheTime = 0)
    public List<InsuranceDicBO> getApplyRangeList() {
        return getEnumList(ApplyRangeEnum.values());
    }

    @Override
    public String getApplyRangeNameByDicId(Integer dicId) {
        return getNameFromList(getApplyRangeList(), dicId);
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_CALCULATE_MODE_KEY, cacheTime = 0)
    public List<InsuranceDicBO> getCalculateModeList() {
        return getEnumList(CalculateModeEnum.values());
    }

    @Override
    public String getCalculateModeNameByDicId(Integer dicId) {

        return getNameFromList(getCalculateModeList(), dicId);
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_FUND_TYPE_KEY, cacheTime = 0)
    public List<InsuranceDicBO> getFundTypeList() {
        return getEnumList(FundTypeEnum.values());
    }

    @Override
    public String getFundTypeNameByDicId(Integer dicId) {
        return getNameFromList(getFundTypeList(), dicId);
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_SETTLE_CONDITION_KEY, cacheTime = 0)
    public List<InsuranceDicBO> getSettleConditionList() {
        return getEnumList(SettleConditionEnum.values());
    }

    @Override
    public String getSettleConditionNameByDicId(Integer dicId) {
        return getNameFromList(getSettleConditionList(), dicId);
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_REBATE_STANDARD_KEY, cacheTime = 0)
    public List<InsuranceDicBO> getRebateStandardList() {
        return getEnumList(RebateStandardEnum.values());
    }

    @Override
    public String getRebateStandardNameByDicId(Integer dicId) {
        return getNameFromList(getRebateStandardList(), dicId);
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_REBATE_TYPE_KEY, cacheTime = 0)
    public List<InsuranceDicBO> getRebateTypeList() {
        return getEnumList(RebateTypeEnum.values());
    }

    @Override
    public String getRebateTypeNameByDicId(Integer dicId) {
        return getNameFromList(getRebateTypeList(), dicId);
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_SCENARIO_TYPE_KEY, cacheTime = 0)
    public List<InsuranceDicBO> getScenarioTypeList() {
        return getEnumList(ScenarioTypeEnum.values());
    }


    @Override
    public String getScenarioTypeNameByDicId(Integer dicId) {
        return getNameFromList(getScenarioTypeList(), dicId);
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_SETTLE_RULE_KEY, cacheTime = 0)
    public List<InsuranceDicBO> geSettleRuleTypeList() {
        return getEnumList(SettleRuleTypeEnum.values());
    }

    @Override
    public String getSettleRuleTypeNameByDicId(Integer dicId) {
        return getNameFromList(geSettleRuleTypeList(), dicId);
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_SETTLE_STATUS_KEY, cacheTime = 0)
    public List<InsuranceDicBO> getSettleStatusList() {
        return getEnumList(SettleStatusEnum.values());
    }

    @Override
    public String getSettleStatusNameByDicId(Integer dicId) {
        return getNameFromList(getSettleStatusList(), dicId);
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_INSURANCE_TYPE_KEY, cacheTime = 0)
    public List<InsuranceDicBO> getInsuranceTypeList() {
        return getEnumList(InsuranceTypeEnum.values());
    }

    @Override
    public String getInsuranceTypeNameByDicId(Integer dicId) {
        return getNameFromList(getInsuranceTypeList(), dicId);
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_CONFIRM_MONEY_STATUS_KEY, cacheTime = 0)
    public List<InsuranceDicBO> getConfirmMoneyStatusList() {
        return getEnumList(ConfirmMoneyStatusEnum.values());
    }

    @Override
    public String getConfirmMoneyStatusNameByDicId(Integer dicId) {
        return getNameFromList(getConfirmMoneyStatusList(), dicId);
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_REWARD_STATUS_KEY, cacheTime = 0)
    public List<InsuranceDicBO> getRewardStatusList() {
        return getEnumList(RewardStatusEnum.values());
    }

    @Override
    public String getRewardStatusNameByDicId(Integer dicId) {
        return getNameFromList(getRewardStatusList(), dicId);
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_WITHDRAW_CASH_STATUS_KEY, cacheTime = 0)
    public List<InsuranceDicBO> getWithdrawCashStatusList() {
        return getEnumList(WithdrawCashStatusEnum.values());
    }

    @Override
    public String getWithdrawCashStatusNameByDicId(Integer dicId) {
        return getNameFromList(getWithdrawCashStatusList(), dicId);
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.FOREVER_BALANCE_STATUS_KEY, cacheTime = 0)
    public List<InsuranceDicBO> getBalanceStatusList() {
        return getEnumList(BalanceStatusEnum.values());
    }

    @Override
    public String getBalanceStatusNameByDicId(Integer dicId) {
        return getNameFromList(getBalanceStatusList(), dicId);
    }



    /*==========private=========*/

    private List<InsuranceDicBO> getEnumList(Enum[] enumArray) {
        return getEnumList(enumArray, "getCode", "getDesc");
    }

    // 反射机制，根据方法名设置值
    private List<InsuranceDicBO> getEnumList(Enum[] enumArray, String getDicIdMethodName, String getDicValMethodName) {
        if (enumArray.length == 0) {
            return Lists.newArrayList();
        }
        Class enumClass = enumArray[0].getClass();

        try {
            Method getIdMethod = enumClass.getDeclaredMethod(getDicIdMethodName);
            Method getValMethod = enumClass.getDeclaredMethod(getDicValMethodName);

            List<InsuranceDicBO> resultList = new ArrayList<>();
            for (Enum enumObj : enumArray) {
                Object dicId = getIdMethod.invoke(enumObj);
                Object dicVal = getValMethod.invoke(enumObj);
                resultList.add(new InsuranceDicBO(Integer.valueOf(dicId.toString()), dicVal.toString()));
            }
            return resultList;
        } catch (NoSuchMethodException e) {
            log.error("methodWrong.Enums:" + Arrays.toString(enumArray) + " getDicIdMethodName:" + getDicIdMethodName + " getDicValMethodName:" + getDicValMethodName, e);
            return Lists.newArrayList();
        } catch (SecurityException e) {
            log.error("methodWrong.Enums:" + Arrays.toString(enumArray) + " getDicIdMethodName:" + getDicIdMethodName + " getDicValMethodName:" + getDicValMethodName, e);
            return Lists.newArrayList();
        } catch (IllegalAccessException e) {
            log.error("methodWrong.Enums:" + Arrays.toString(enumArray) + " getDicIdMethodName:" + getDicIdMethodName + " getDicValMethodName:" + getDicValMethodName, e);
            return Lists.newArrayList();
        } catch (IllegalArgumentException e) {
            log.error("methodWrong.Enums:" + Arrays.toString(enumArray) + " getDicIdMethodName:" + getDicIdMethodName + " getDicValMethodName:" + getDicValMethodName, e);
            return Lists.newArrayList();
        } catch (InvocationTargetException e) {
            log.error("methodWrong.Enums:" + Arrays.toString(enumArray) + " getDicIdMethodName:" + getDicIdMethodName + " getDicValMethodName:" + getDicValMethodName, e);
            return Lists.newArrayList();
        }

    }

    private String getNameFromList(List<InsuranceDicBO> list, Integer dicId) {
        for (InsuranceDicBO dicBO : list) {
            if (dicBO.getDicId().equals(dicId)) {
                return dicBO.getDicValue();
            }
        }
        return null;
    }
}
