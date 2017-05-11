package com.tqmall.mana.client.beans.param;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * 结算金额计算参数
 *
 * Created by huangzhangting on 17/3/10.
 */
@Data
public class SettleFeeCalculateParam implements Serializable {
    private String formulaKey; //计算公式key
    private BigDecimal bizInsuranceFee; //商业险保费
    private BigDecimal forceInsuranceFee; //交强险保费
    private BigDecimal couponAmount; //现金券金额
    private Integer insuranceCompanyId; //保险公司id
    private String cityCode; //投保城市编码
}
