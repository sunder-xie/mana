package com.tqmall.mana.beans.BO.sys;

import lombok.Data;

/**
 * Created by huangzhangting on 16/12/25.
 */
@Data
public class ModifyRoleBO extends AddRoleBO {
    private Integer id;
    private boolean simpleModify = true;  //简单修改，只修改基本信息，不修改关联的资源
}
