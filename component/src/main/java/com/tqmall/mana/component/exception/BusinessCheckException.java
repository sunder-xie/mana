package com.tqmall.mana.component.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务检查异常，例如参数检验时，直接抛出业务检查异常即可
 * Created by huangzhangting on 16/12/24.
 */
public class BusinessCheckException extends RuntimeException {
    @Setter
    @Getter
    private String code;
    @Setter
    @Getter
    private String message;

    public BusinessCheckException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "BusinessCheckException{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
