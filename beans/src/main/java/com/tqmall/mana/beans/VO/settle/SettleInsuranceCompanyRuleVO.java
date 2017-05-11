package com.tqmall.mana.beans.VO.settle;

import com.tqmall.mana.component.annotation.IgnoreCheckAnnotation;
import lombok.Data;

import java.util.List;

/**
 * Created by zengjinju on 17/1/14.
 */
@Data
public class SettleInsuranceCompanyRuleVO {
    /**
     * 保险公司结算规则基础表id
     **/
    @IgnoreCheckAnnotation(isIgnore = true)
    private Integer id;

    /**
     * 保险公司id
     **/
    private Integer insuranceCompanyId;

    /**
     * 保险公司名称
     **/
    private String insuranceCompanyName;

    /**
     * 省份编码
     **/
    private String provinceCode;

    /**
     * 省份名称
     **/
    private String provinceName;

    /**
     * 城市编码
     **/
    private List<String> cityCodeList;

    /**
     * 城市名称
     **/
    private List<String> cityNameList;

    /**投保场景**/
    List<SettleInsuranceCompanyRuleItemVO> ruleItemList;


}
