package com.tqmall.mana.component.util;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.common.errorcode.ErrorCode;

import java.util.List;

/**
 * Created by huangzhangting on 16/1/26.
 */
public class ResultUtil {
    public static <T> Result<T> errorResult(String code, String message) {
        return new Result<T>().setSuccess(false).setCode(code).setMessage(message);
    }

    public static <T> Result<T> errorResult(ErrorCode errorCode) {
        return new Result<T>().setSuccess(false)
                .setCode(errorCode.getCode())
                .setMessage(errorCode.getErrorMessage());
    }

    public static <T> Result<T> successResult(T data) {
        return Result.wrapSuccessfulResult(data);
    }

    public static <T> Result<T> successResult(T data, String message) {
        return Result.wrapSuccessfulResult(message, data);
    }

    public static <BO, DTO> Result<DTO> successResult(BO data,Class<DTO> boClass) {

        return Result.wrapSuccessfulResult(BdUtil.do2bo(data,boClass));
    }


    public static <BO, DTO> Result<List<DTO>> successListResult(List<BO> data,Class<DTO> boClass) {

        return Result.wrapSuccessfulResult(BdUtil.do2bo4List(data, boClass));
    }


    public static <DO, BO> Result<BO> handleResult(Result<DO> result, Class<BO> boClass){
        if(result.isSuccess()){
            return successResult(BdUtil.do2bo(result.getData(), boClass));
        }
        return errorResult(result.getCode(), result.getMessage());
    }
    public static <DO, BO> Result<List<BO>> handleResultList(Result<List<DO>> result, Class<BO> boClass){
        if(result.isSuccess()){
            return successResult(BdUtil.do2bo4List(result.getData(), boClass));
        }
        return errorResult(result.getCode(), result.getMessage());
    }

    public static <T> PagingResult<T> errorPageResult(ErrorCode errorCode){
        return PagingResult.wrapErrorResult(errorCode.getCode(), errorCode.getErrorMessage());
    }

}
