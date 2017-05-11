package com.tqmall.mana.beans.VO.settle.shopRule;

import com.tqmall.mana.beans.VO.settle.shopRule.add.AddSettleShopRuleItemVO;
import lombok.Data;

import java.util.List;

/**
 * Created by huangzhangting on 17/1/18.
 */
@Data
public class ModifyShopRuleVO {
    /** 数据库主键id */
    private Integer id;
    /** 结算条件 1:签单日期 2:起保日期 3:服务包支付日期 */
    private Integer settleCondition;
    /** 配置地区 */
    private List<SettleShopRuleRegionConfigVO> regionConfigVOList;

    /** 服务包规则 */
    private AddSettleShopRuleItemVO packageRuleItem;
    /** 商业险规则 */
    private List<AddSettleShopRuleItemVO> bizRuleItemList;
    /** 交强险规则 */
    private List<AddSettleShopRuleItemVO> forceRuleItemList;

}
