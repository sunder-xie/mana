package com.tqmall.mana.beans.BO.settle;

import lombok.Data;

import java.util.Date;

/**
 *
 * 修改门店规则明细
 *
 * Created by huangzhangting on 17/1/18.
 */
@Data
public class DeleteShopRuleItemBO {
    private Date gmtModified;
    private String modifier;
    private Integer settleRuleId;

}
