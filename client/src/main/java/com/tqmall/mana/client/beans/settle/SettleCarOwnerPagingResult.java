package com.tqmall.mana.client.beans.settle;

import com.tqmall.core.common.entity.PagingResult;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by jinju.zeng on 17/1/22.
 */
@Data
public class SettleCarOwnerPagingResult implements Serializable {
    private PagingResult<SettleCarOwnerCheckerDetailDTO> pagingResult;
    //1期收款合计
    private BigDecimal firstPayTotalAmount;

    //2期收款合计
    private BigDecimal secondPayTotalAmount;

    //应交保费总额
    private BigDecimal totalPayAbleInsuredFee;

    //应交合计
    private BigDecimal payableTotalAmount;
}
