package com.tqmall.mana.beans.BO.settle;

import lombok.Data;


/**
 * Created by huangzhangting on 17/3/7.
 */
@Data
public class SettleFeeFormulaConfigBO {
    //主键ID
    private Integer id;

    //是否删除,Y删除,N未删除
    private String isDeleted;

    //公式key
    private String formulaKey;

    //公式名称
    private String formulaName;

    //计算公式
    private String formulaExpress;

    //公式说明
    private String formulaExplain;

    //是否展示 0:否 1:是
    private Integer isShow;

}
