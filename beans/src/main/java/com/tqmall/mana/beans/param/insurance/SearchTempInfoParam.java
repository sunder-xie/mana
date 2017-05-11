package com.tqmall.mana.beans.param.insurance;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by zhouheng on 17/3/28.
 */
@Data
public class SearchTempInfoParam {

    /**
     * 隐藏条件:legend端保单录入时必须传
     */
    private Integer agentId;
    /**
     * 车牌号码
     **/
    private String vehicleSn;

    /**
     * 卖保险代理人名称
     **/
    private String agentName;

    /**买保险人手机号**/
    private String agentMobile;

    /**
     * 审核日期开始阶段
     **/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date gmtCreateStart;

    /**
     * 审核日期结束阶段
     **/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date gmtCreateEnd;

    /**
     * 保单审核状态, 0:未审核 1:审核通过 2:审核驳回
     **/
    private Integer auditStatus;

    private Integer pageNum = 1;//页码

    private Integer pageSize = 10;//每页数量(默认10条)

}
