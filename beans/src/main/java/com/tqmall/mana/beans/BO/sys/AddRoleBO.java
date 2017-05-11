package com.tqmall.mana.beans.BO.sys;

import lombok.Data;

import java.util.List;

/**
 * Created by huangzhangting on 16/12/25.
 */
@Data
public class AddRoleBO {
    private String roleName;
    private String roleDescription;
    private Integer roleSort;
    private List<Integer> resourceIds;
}
