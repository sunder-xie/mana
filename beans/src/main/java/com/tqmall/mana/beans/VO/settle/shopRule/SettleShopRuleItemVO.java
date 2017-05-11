package com.tqmall.mana.beans.VO.settle.shopRule;

import com.tqmall.mana.beans.VO.settle.SettleConfigBasicVO;
import lombok.Data;

import java.util.Date;

@Data
public class SettleShopRuleItemVO {
    private Integer id;

    private Integer settleRuleId;

    /** 结算条件 1:签单日期 2:起保日期 3:服务包支付日期 */
    private Integer settleCondition;

    /** 返点基准 1:交强险 2:商业险 3:服务包 */
    private Integer rebateStandard;

    /** 资金类型 1:现金 2:奖励金 */
    private Integer fundType;

    /** 返点类型 1:比例 2:服务包工时费 */
    private Integer rebateType;

    /** 计算方式 1:单笔 2:月累计 */
    private Integer calculateMode;

    /** 计算时间 */
    private Date calculateTime;

    /** 保费区间分组id */
    private Integer settleConfigBasicId;

    private Date startTime;

    private Date endTime;

    /** 保费区间组实体 */
    private SettleConfigBasicVO settleConfigBasicVO;

}