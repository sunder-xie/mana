package com.tqmall.mana.beans.VO.settle.shopRule;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SettleShopRuleRebateStandardVO {

    private Integer settleRuleId;

    /** 返点基准 1:交强险 2:商业险 3:服务包 */
    private Integer rebateStandard;
    private String rebateStandardName;

    /** 结算条件 1:签单日期 2:起保日期 3:服务包支付日期 */
    private Integer settleCondition;
    private String settleConditionName;

    /** 资金类型 1:现金 2:奖励金 */
    private Integer fundType;
    private String fundTypeName;

    /** 返点类型 1:比例 2:服务包工时费 */
    private Integer rebateType;
    private String rebateTypeName;

    /** 计算方式 1:单笔 2:月累计 */
    private Integer calculateMode;
    private String calculateModeName;

    /** 计算时间 */
    private Date calculateTime;


    /** 有效期区间组 */
    List<SettleShopRuleConfigBasicVO> configBasicVOList;


    /** 合并行数，前端使用 */
    private int rowSpan = 0;

}