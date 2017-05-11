package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.util.Date;

@Data
public class SettleScenarioInsuranceCategoryRelDO {
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

    //保险公司id
    private Integer insuranceCompanyId;

    //投保场景 1:单商业险 2:商业交强同保 3:单交强险 4:第三方责任交强同保 5:责任险单保
    private Byte scenarioType;

    //投保场景权重值
    private Integer scenarioWeight;

    //保险项目编码
    private String insuranceCategoryCode;

    //保险项目名称
    private String insuranceCategoryName;

}