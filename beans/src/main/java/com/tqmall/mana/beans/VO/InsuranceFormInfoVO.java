package com.tqmall.mana.beans.VO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 车主详情-业务信息-保单相关数据
 * Created by huangzhangting on 16/12/29.
 */
@Data
public class InsuranceFormInfoVO {

    /** 投保模式 */
    private String insureTypeStr;
    /** 保费 */
    private BigDecimal insuredFee;

    /** 真实保单id */
    private Integer formId;

}
