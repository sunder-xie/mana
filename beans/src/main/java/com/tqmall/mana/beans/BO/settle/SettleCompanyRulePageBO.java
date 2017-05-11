package com.tqmall.mana.beans.BO.settle;

import com.tqmall.mana.beans.VO.settle.SettleInsuranceCompanyRuleItemVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by zengjinju on 17/1/16.
 */
@Data
public class SettleCompanyRulePageBO {
    private Integer id;
    private String insuranceCompanyName;
    private String provinceName;
    private String cityName;
    private Date gmtCreate;
    List<SettleInsuranceCompanyRuleItemVO> ruleItemList;
}
