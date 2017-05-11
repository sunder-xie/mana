package com.tqmall.mana.beans.BO.settle;

import lombok.Data;
import java.math.BigDecimal;

/**
 * Created by zengjinju on 17/1/22.
 */
@Data
public class SettleCompanyAmountSumBO {
    //保费合计
    private BigDecimal totalInsuredFee;

    //保费分成合计
    private BigDecimal totalInsuredRoyaltyFee;
}
