package com.tqmall.mana.beans.entity.sys;

import lombok.Data;

import java.util.Date;

@Data
public class ManaSysRoleResourceDO {
    private Integer id;

    private Date gmtCreate;

    private String creator;

    private Integer resourceId;

    private Integer roleId;

}