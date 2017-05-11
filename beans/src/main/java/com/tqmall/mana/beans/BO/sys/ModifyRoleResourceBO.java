package com.tqmall.mana.beans.BO.sys;

import lombok.Data;

import java.util.List;

/**
 * Created by huangzhangting on 16/12/19.
 */
@Data
public class ModifyRoleResourceBO {
    private Integer roleId;
    private List<Integer> resourceIds;
    private boolean isModify = true;
}
