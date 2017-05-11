package com.tqmall.mana.client.beans.settle;

import lombok.Data;

import java.io.Serializable;

@Data
public class SettleFeeFormulaConfigDTO implements Serializable{
    //主键ID
    private Integer id;

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