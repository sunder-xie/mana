package com.tqmall.mana.beans.VO;

import lombok.Data;

import java.util.List;

/**
 * Created by huangzhangting on 16/12/25.
 */
@Data
public class ModifyRoleVO {
    private Integer id;
    private String roleName;
    private String roleDescription;
    private Integer roleSort;
    private List<ZTree> resourceList;
}
