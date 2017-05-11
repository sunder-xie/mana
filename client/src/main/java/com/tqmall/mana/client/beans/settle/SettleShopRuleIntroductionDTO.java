package com.tqmall.mana.client.beans.settle;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhouheng on 17/2/7.
 */
@Data
public class SettleShopRuleIntroductionDTO implements Serializable {

    private Integer id;

    private Date gmtModified;

    private Integer ruleType;

    private String ruleIntroduction;

    private Integer sortIndex;
}
