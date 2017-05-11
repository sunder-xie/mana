package com.tqmall.mana.biz.mq.insurance;

import lombok.Data;

/**
 * Created by huangzhangting on 16/12/21.
 */
@Data
public class InsuranceFormMsg {
    private Integer id;
    private java.util.Date gmtCreate;

    /**起保日期**/
    private java.util.Date packageStartTime;

    /**终保日期**/
    private java.util.Date packageEndTime;

    /**和淘气合作模式 1:奖励金 2:买保险送服务 3:买服务送保险**/
    private Integer cooperationMode;

    /** 虚拟保单id */
    private Integer virtualFormId;

}
