package com.tqmall.mana.beans.BO.settle;

import lombok.Data;

import java.util.Date;

/**
 *
 * 修改地区配置
 *
 * Created by huangzhangting on 17/1/18.
 */
@Data
public class DeleteRegionConfigBO {
    private Date gmtModified;
    private String modifier;
    private Integer settleRuleId;

}
