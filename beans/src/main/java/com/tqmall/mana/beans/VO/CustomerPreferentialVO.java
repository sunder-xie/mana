package com.tqmall.mana.beans.VO;

import lombok.Data;

import java.util.Date;

/**
 * 客户优惠信息
 * Created by huangzhangting on 17/1/3.
 */
@Data
public class CustomerPreferentialVO {
    private Date sendDate; //发送优惠的日期
    private String preferentialContent; //优惠内容，程序中根据不同需求组装好给前端直接展示
    private String creator; //创建人
}
