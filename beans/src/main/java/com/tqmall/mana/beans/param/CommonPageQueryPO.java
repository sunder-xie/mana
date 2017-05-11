package com.tqmall.mana.beans.param;

import lombok.Data;

/**
 *
 * 通用的分页查询参数
 *
 * Created by huangzhangting on 17/1/21.
 */
@Data
public class CommonPageQueryPO {

    /* 分页查询最大条数 */
    public static final int MAX_PAGE_SIZE = 200;
    /* 分页查询默认条数 */
    public static final int DEFAULT_PAGE_SIZE = 10;

    private Integer pageIndex; //页码，从 1 开始
    private Integer pageSize; //每页条数
    private Integer offerSet; //分页查询偏移量，数据库使用


    //处理分页参数
    public void handlePageParam(){
        if(pageIndex==null || pageIndex<1){
            pageIndex = 1;
        }
        if(pageSize==null || pageSize<1){
            pageSize = CommonPageQueryPO.DEFAULT_PAGE_SIZE;
        }else if(pageSize>CommonPageQueryPO.MAX_PAGE_SIZE){
            pageSize = CommonPageQueryPO.MAX_PAGE_SIZE;
        }
        offerSet = (pageIndex - 1) * pageSize;
    }

}
