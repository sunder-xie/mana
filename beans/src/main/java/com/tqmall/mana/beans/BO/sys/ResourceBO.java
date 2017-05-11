package com.tqmall.mana.beans.BO.sys;

import lombok.Data;

import java.util.List;

/**
 * Created by huangzhangting on 16/12/24.
 */
@Data
public class ResourceBO {
    private Integer id;

    private Integer parentId;

    private String resourceName;

    private String resourceUrl;

    private Integer resourceType;
    private String typeString;

    private String resourceDescription;

    private Integer resourceSort;

    private String resourcePermission;

    private boolean selected = false;

    private List<ResourceBO> children;
}
