package com.tqmall.mana.component.bean;

import com.tqmall.core.common.errorcode.ErrorCode;
import com.tqmall.core.common.errorcode.ErrorCodeBuilder;
import com.tqmall.core.common.errorcode.PlatformErrorCode;
import com.tqmall.core.common.errorcode.support.CommonError;

/**
 * 项目异常定义
 * Created by huangzhangting on 16/1/25.
 */
public class DataError extends CommonError {
    private static final String SYSTEM_CODE = "05";

    public static final ErrorCode UN_KNOW_EXCEPTION = ErrorCodeBuilder.newError(PlatformErrorCode.DATA, SYSTEM_CODE)
            .setError()
            .setDetailCode("0001")
            .setName("未知异常")
            .setMessageFormat("非常抱歉，系统发生内部错误")
            .genErrorCode();

    public static final ErrorCode NO_DATA_ERROR = ErrorCodeBuilder.newError(PlatformErrorCode.DATA, SYSTEM_CODE)
            .setError()
            .setDetailCode("0002")
            .setName("未查到数据")
            .setMessageFormat("抱歉，没有查到相关数据")
            .genErrorCode();
}
