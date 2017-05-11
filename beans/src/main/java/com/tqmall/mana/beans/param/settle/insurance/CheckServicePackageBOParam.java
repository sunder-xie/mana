package com.tqmall.mana.beans.param.settle.insurance;

import lombok.Data;

/**
 * Created by zhouheng on 17/2/24.
 */
@Data
public class CheckServicePackageBOParam {

    /**服务包名称**/
    private String packageName;

    private Integer pageNum ;//页码

    private Integer pageSize;//每页数量(默认15条)

}
