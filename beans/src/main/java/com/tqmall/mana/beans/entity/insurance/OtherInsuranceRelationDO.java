package com.tqmall.mana.beans.entity.insurance;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class OtherInsuranceRelationDO {
    private Integer id;

    private String isDeleted;

    private Date gmtModified;

    private Date gmtCreate;

    private String anxinInsuranceName;

    private String pingAnInsuranceName;

    private String pingAnDeductible;

    private String renInsuranceName;

    private String renDeductible;
}