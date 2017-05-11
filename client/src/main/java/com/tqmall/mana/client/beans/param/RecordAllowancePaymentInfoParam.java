package com.tqmall.mana.client.beans.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zwb on 17/2/21.
 */
@Data
public class RecordAllowancePaymentInfoParam implements Serializable {
    /**订单编号**/
    private List<String> listOrderSn;

    /**修改人**/
    private String modifier;

    /**请款状态**/
    private Integer allowanceStatus;
}
