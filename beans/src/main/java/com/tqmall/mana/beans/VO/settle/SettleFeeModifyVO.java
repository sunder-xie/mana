package com.tqmall.mana.beans.VO.settle;

import com.tqmall.mana.component.util.DateUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by huangzhangting on 17/3/10.
 */
@Data
public class SettleFeeModifyVO {
    private String insuranceOrderSn; //淘汽保单号
    private String couponSn; //现金券编号
    private BigDecimal couponAmount; //现金券金额

    @DateTimeFormat(pattern = DateUtil.yyyy_MM_dd_HH_mm_ss)
    private Date couponCashTime; //现金券兑现时间

    private BigDecimal bizInsuranceRate; //商业险比例
    private BigDecimal forceInsuranceRate; //交强险比例

}
