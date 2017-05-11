package com.tqmall.mana.component.exception;

/**
 * 业务异常封装类
 * Created by huangzhangting on 16/12/24.
 */
public class BusinessException extends RuntimeException {
    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
}
