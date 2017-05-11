package com.tqmall.mana.client.beans.otherInsurance;

import com.tqmall.core.common.entity.PagingResult;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by zwb on 17/2/21.
 */
@Setter
@Getter
public class CheckAllowanceDTO implements Serializable {
    private static final long serialVersionUID = -7234978586929118950L;

    /**应补贴金额总和**/
    private BigDecimal payableAmountCount;

    /**查询列表显示**/
    PagingResult<InsuranceMaterialAllowanceDTO> allowanceDTOPagingResult;

}
