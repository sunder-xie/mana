package com.tqmall.mana.beans.VO.settle;

import com.tqmall.core.common.entity.PagingResult;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zengjinju on 17/1/22.
 */
@Data
public class SettleCompanyBizPagingResult {

    private PagingResult<SettleCompanyCheckDetailVO> pagingResult;

    //保费合计
    private BigDecimal totalInsuredFee;

    //保费分成合计
    private BigDecimal totalInsuredRoyaltyFee;
}
