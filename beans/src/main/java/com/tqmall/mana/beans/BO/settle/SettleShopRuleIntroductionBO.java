package com.tqmall.mana.beans.BO.settle;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhouheng on 17/2/7.
 */
@Data
public class SettleShopRuleIntroductionBO {

    private Integer id;

    private Date gmtModified;

    private Integer ruleType;

    private String ruleIntroduction;

    private Integer sortIndex;
}
