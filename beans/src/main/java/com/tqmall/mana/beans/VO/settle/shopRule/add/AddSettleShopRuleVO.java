package com.tqmall.mana.beans.VO.settle.shopRule.add;

import com.tqmall.mana.beans.VO.settle.shopRule.SettleShopRuleRegionConfigVO;
import com.tqmall.mana.beans.VO.settle.shopRule.SettleShopRuleShopConfigVO;
import lombok.Data;

import java.util.List;

/**
 * Created by huangzhangting on 17/2/22.
 */
@Data
public class AddSettleShopRuleVO {
    /** 保险公司id */
    private Integer insuranceCompanyId;
    /** 保险公司名称 */
    private String insuranceCompanyName;

    /** 适用范围 1:地区 2:门店 */
    private Integer applyRange;

    /** 保险模式 1:奖励金 2:买保险送服务 3:买服务送保险 */
    private Integer cooperationMode;

    /** 地区配置 applyRange=1 */
    private List<SettleShopRuleRegionConfigVO> regionConfigVOList;

    /** 门店配置 applyRange=2 */
    private SettleShopRuleShopConfigVO shopConfigVO;

    /** 商业险规则 */
    private List<AddSettleShopRuleItemVO> bizRuleItemList;
    /** 交强险规则 */
    private List<AddSettleShopRuleItemVO> forceRuleItemList;


    /** 服务包规则 （服务包模式使用该参数） */
    private AddSettleShopRuleItemVO packageRuleItem;


    /** 结算条件 1:签单日期 2:起保日期 3:服务包支付日期 （奖励金模式使用该参数） */
    private Integer settleCondition;

}
