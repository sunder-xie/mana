package com.tqmall.mana.beans.VO.settle.shopRule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tqmall.mana.beans.VO.settle.SettleConfigBasicVO;
import lombok.Data;

import java.util.Date;

@Data
public class SettleShopRuleConfigBasicVO {
    private Integer id;

    /** 保费区间分组id */
    private Integer settleConfigBasicId;

    private Date startTime;

    private Date endTime;

    /** 保费区间组实体 */
    private SettleConfigBasicVO settleConfigBasicVO;


    /** 合并行数，前端使用 */
    private int rowSpan = 0;

    /** 上层对象，方便组装合并行数，不返回给前端 */
    @JsonIgnore
    private SettleShopRuleRebateStandardVO rebateStandardVO;

}