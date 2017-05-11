package com.tqmall.mana.client.beans.settle;

import com.tqmall.core.common.entity.PagingResult;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by zengjinju on 17/1/22.
 */
@Data
public class SettleCompanyCheckPagingResult implements Serializable {

    private PagingResult<SettleCompanyCheckDetailDTO> pagingResult;

    //保费合计
    private BigDecimal totalInsuredFee;

    //保费分成合计
    private BigDecimal totalInsuredRoyaltyFee;
}
