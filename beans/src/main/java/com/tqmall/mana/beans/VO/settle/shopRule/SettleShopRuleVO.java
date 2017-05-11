package com.tqmall.mana.beans.VO.settle.shopRule;

import lombok.Data;

import java.util.List;

/**
 * Created by huangzhangting on 17/1/14.
 */
@Data
public class SettleShopRuleVO {
    private Integer id;

    private Integer insuranceCompanyId;

    private String insuranceCompanyName;

    /** 适用范围 1:地区 2:门店 */
    private Integer applyRange;

    /** 和淘气合作模式 1:奖励金 2:买保险送服务 3:买服务送保险 */
    private Integer cooperationMode;
    private String cooperationModeName;

    /** 地区配置 applyRange=1 */
    private List<SettleShopRuleRegionConfigVO> regionConfigVOList;

    /** 门店配置 applyRange=2 */
    private SettleShopRuleShopConfigVO shopConfigVO;

//    private List<SettleShopRuleItemVO> ruleItemVOList;

    /** 结算条件名称，奖励金模式，前端使用 */
    private String settleConditionName;
    private Integer settleCondition;

    /** 配置地区 或者 门店账号及名称，前端使用 */
    private String applyRangeString;

    /** 返点基础，包含：商业险、交强险、服务包 */
    private List<SettleShopRuleRebateStandardVO> rebateStandardVOs;


    /** 合并行数，前端使用 */
    private int rowSpan = 0;

}
