package com.tqmall.mana.beans.VO.settle;

import com.tqmall.mana.component.util.DateUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by huangzhangting on 17/3/31.
 */
@Data
public class AddMaterialAllowanceVO {
    private String insuranceOrderSn; //淘汽保单号
    private String materialOrderSn; //物料订单号

    private BigDecimal payableAmount; //应补贴金额

    @DateTimeFormat(pattern = DateUtil.yyyy_MM_dd_HH_mm_ss)
    private Date allowanceEffectTime; //补贴生效时间

    private Integer materialNum; //物料数量
    private Integer whsId; //订单仓库Id
    private String whsName; //订单仓库名字

}
