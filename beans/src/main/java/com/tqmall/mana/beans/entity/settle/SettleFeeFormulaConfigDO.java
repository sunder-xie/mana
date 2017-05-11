package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.util.Date;

@Data
public class SettleFeeFormulaConfigDO {
    //主键ID
    private Integer id;

    //是否删除,Y删除,N未删除
    private String isDeleted;

    //创建时间
    private Date gmtCreate;

    //更新时间
    private Date gmtModified;

    //创建人
    private String creator;

    //修改人
    private String modifier;

    //公式名称
    private String formulaName;

    //计算公式
    private String formulaExpress;

    //公式说明
    private String formulaExplain;

    //公式key
    private String formulaKey;

    //是否展示 0:否 1:是
    private Integer isShow;

}