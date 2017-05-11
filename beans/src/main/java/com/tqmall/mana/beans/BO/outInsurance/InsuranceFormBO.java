package com.tqmall.mana.beans.BO.outInsurance;

import com.tqmall.mana.beans.BO.InsuranceBasicBO;
import com.tqmall.mana.beans.BO.insurance.InsuranceTypeBO;
import com.tqmall.mana.component.enums.FormStatusEnum;
import com.tqmall.mana.component.enums.insurance.dict.InsuranceTypeEnum;
import com.tqmall.mana.component.enums.QuitStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouheng on 16/12/20.
 */
@Getter
@Setter
public class InsuranceFormBO {

    /**
     * 主键ID
     **/
    private Integer insuranceFormId;

    /**
     * 创建时间
     **/
    private Date gmtCreate;

    /**
     * 门店类型1:门店,2:个人
     */
    private Integer agentType;

    /**
     * 代理人ID
     */
    private Integer agentId;

    /**
     * 门店信息(门店名称+名称电话)
     */
    private String agentName;

    /**
     * 保单号:缴费完成返回
     **/
    private String outerInsuranceFormNo;

    /**
     * 保险类别:1表示交强险,2表示商业险
     **/
    private Integer insuranceType;

    /**
     * 前端使用:
     */
    private String insuranceTypeDescription;

    /**
     * 保费,投保人投保的保费
     **/
    private BigDecimal insuredFee;

    /**
     * 起保日期
     **/
    private Date packageStartTime;

    /**
     * 终保日期
     **/
    private Date packageEndTime;

    /**
     * 退保状态, 0:未退保； 1:已退保
     **/
    private Integer quitStatus;
    /**
     * 前端展示用到
     */
    private String quitStatusName;

    /**
     * 保单状态, 0:暂存（点击保费计算） 1:已提交 2:待缴费 3:已缴费
     **/
    private Integer insureStatus;

    /**
     * 前端展示用到
     */
    private String formStatusName;

    /**
     * 和淘气合作模式 1:奖励金 2:买保险送服务 3:买服务送保险
     **/
    private Integer cooperationMode;
    //前端使用:
    private String cooperationModeDescription;

    /**
     * 缴费时间
     **/
    private Date payTime;

    private List<InsuranceTypeBO> insuranceTypeBOList;

    private InsuranceBasicBO basicBO;

    private List<InsuranceItemBO> itemBOList;

    /**
     * 险种页面显示调用
     **/
    public String getInsuranceTypeDescription(Integer insuranceType) {
        if (insuranceTypeDescription != null) {
            return insuranceTypeDescription;
        }
        return InsuranceTypeEnum.codeDescription(insuranceType);
    }

    public String getFormStatusName() {
        if (formStatusName != null) {
            return formStatusName;
        }
        return FormStatusEnum.codeDescription(insureStatus);
    }


    public String getQuitStatusName() {
        if (quitStatusName != null) {
            return quitStatusName;
        }
        return QuitStatusEnum.codeDescription(quitStatus);
    }

//    /**
//     * 合作模式页面显示调用
//     **/
//    public String getCooperationModeDescription(Integer cooperationMode) {
//        if (cooperationModeDescription != null) {
//            return cooperationModeDescription;
//        }
//        return CooperationModeEnum.codeDescription(cooperationMode);
//    }
}
