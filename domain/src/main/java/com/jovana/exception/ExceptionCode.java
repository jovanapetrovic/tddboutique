package com.jovana.exception;

/**
 * Created by jovana on 24.02.2020
 */
public enum ExceptionCode {
    USERNAME_ALREADY_IN_USE(1000),

    ENTITY_NOT_FOUND_IN_DB(2000),

    ;

    private Integer code;

    ExceptionCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
