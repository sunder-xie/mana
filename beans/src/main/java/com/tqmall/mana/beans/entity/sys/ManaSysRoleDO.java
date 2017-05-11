package com.tqmall.mana.beans.entity.sys;

import lombok.Data;

import java.util.Date;

@Data
public class ManaSysRoleDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private String roleName;

    private String roleDescription;

    private Integer roleSort;

}