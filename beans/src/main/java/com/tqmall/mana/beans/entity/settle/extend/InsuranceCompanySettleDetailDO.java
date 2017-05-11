package com.tqmall.mana.beans.entity.settle.extend;

import com.tqmall.mana.beans.entity.settle.SettleCompanyCheckDetailDO;
import lombok.Data;

/**
 * Created by huangzhangting on 17/2/7.
 */
@Data
public class InsuranceCompanySettleDetailDO extends SettleCompanyCheckDetailDO{

    //保险模式
    private Integer cooperationModeId;
    //险种
    private Integer insuranceTypeId;

}
