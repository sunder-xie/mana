package com.tqmall.mana.beans.entity.sys;

import lombok.Data;

import java.util.Date;

@Data
public class ManaSysUserRoleDO {
    private Integer id;

    private Date gmtCreate;

    private String creator;

    private String userName;

    private Integer roleId;

}