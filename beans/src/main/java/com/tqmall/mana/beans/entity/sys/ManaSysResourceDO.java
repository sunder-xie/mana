package com.tqmall.mana.beans.entity.sys;

import lombok.Data;

import java.util.Date;

@Data
public class ManaSysResourceDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private Integer parentId;

    private String resourceName;

    private String resourceUrl;

    private Integer resourceType;

    private String resourceDescription;

    private Integer resourceSort;

    private String resourcePermission;

}