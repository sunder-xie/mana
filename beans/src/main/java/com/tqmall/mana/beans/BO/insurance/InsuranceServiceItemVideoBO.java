package com.tqmall.mana.beans.BO.insurance;

import lombok.Data;

/**
 * Created by zhouheng on 17/2/20.
 */
@Data
public class InsuranceServiceItemVideoBO {

    /**
     * 主键ID
     **/
    private Integer id;

    /**
     * 创建时间
     **/
    private java.util.Date gmtCreate;

    /**
     * 服务项id
     **/
    private Integer serviceItemId;

    /**
     * 视频名称
     **/
    private String videoName;

    /**
     * 视频教程
     **/
    private String videoTutorial;

}
