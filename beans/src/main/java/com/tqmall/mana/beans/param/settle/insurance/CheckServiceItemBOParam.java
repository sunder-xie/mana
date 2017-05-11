package com.tqmall.mana.beans.param.settle.insurance;

import lombok.Data;

/**
 * Created by zhouheng on 17/2/24.
 */
@Data
public class CheckServiceItemBOParam {

    /**服务项名称**/
    private String itemName;

    private Integer pageNum ;//页码

    private Integer pageSize ;//每页数量(默认15条)

}
