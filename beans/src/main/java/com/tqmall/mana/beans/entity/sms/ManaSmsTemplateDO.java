package com.tqmall.mana.beans.entity.sms;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ManaSmsTemplateDO implements Serializable {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private String templateKey;

    private String templateContent;

    private String templateDesc;

}