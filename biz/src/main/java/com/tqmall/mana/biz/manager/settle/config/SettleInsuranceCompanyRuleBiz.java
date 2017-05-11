package com.tqmall.mana.biz.manager.settle.config;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.result.InsuranceRegionDTO;
import com.tqmall.mana.beans.BO.settle.SettleCompanyRulePageBO;
import com.tqmall.mana.beans.VO.settle.SettleCompanyRuleSearchVO;
import com.tqmall.mana.beans.VO.settle.SettleInsuranceCompanyRuleVO;
import com.tqmall.mana.beans.param.settle.SettleShopRuleRegionParam;
import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettleBasicMsg;
import com.tqmall.mana.component.enums.insurance.dict.ScenarioTypeEnum;

import java.util.List;

/**
 * 保险公司结算规则配置业务代码
 * <p>
 * Created by huangzhangting on 17/1/13.
 */
public interface SettleInsuranceCompanyRuleBiz {
    /**
     * 获取开通的投保城市站
     * if regionParnetCode!="000000"
     * 获取当前省份未配置过分成比例的的城市
     *
     * @param regionParentCode
     * @return
     */
    Result<List<InsuranceRegionDTO>> getInsuredRegionAndIsOpen(String regionParentCode);

    /**
     * 获取投保场景
     *
     * @return
     */
    List<ScenarioTypeEnum> getScenarioTypes();

    /**
     * 保存保险公司结算规则信息(包括修改操作)
     *
     * @param settleInsuranceCompanyRuleVO
     * @return
     */
    Result<Boolean> saveInsuranceCompanyRule(SettleInsuranceCompanyRuleVO settleInsuranceCompanyRuleVO);

    /**
     * 根据id删除城市返点比例配置
     *
     * @param id
     * @return
     */
    Boolean deleteInsuranceCompanyRuleById(Integer id);

    /**
     * 获取保险公司结算规则分页数据
     *
     * @param
     * @param
     * @return
     */
    PagingResult<SettleCompanyRulePageBO> searchInsuranceCompanyRuleForPage(SettleCompanyRuleSearchVO searchVO);

    /**
     * 获取城市站下拉列表
     *
     * @param regionParentCode
     * @return
     */
    Result<List<InsuranceRegionDTO>> getRegionList(String regionParentCode);

    /**
     * 根据id获取返点比例配置信息
     *
     * @param id
     * @return
     */
    Result<SettleInsuranceCompanyRuleVO> getInsuranceCompanyRuleById(Integer id);

    /**
     * 根据保单的具体信息获取投保场景
     *
     * @param basicMsg
     * @return
     */
    Integer getScenarioType(InsuranceSettleBasicMsg basicMsg);

    /**
     * 计算保费分成(既保险公司给淘气的钱)
     *
     * @param basicMsg
     * @return
     */
    Boolean calculateInsuredRoyaltyFee(InsuranceSettleBasicMsg basicMsg);

    /**
     *
     * @return
     */
    Result<List<InsuranceRegionDTO>> getAvailableRegionList(SettleShopRuleRegionParam settleShopRuleRegionParam);
}
