package com.jovana.exception;

/**
 * Created by jovana on 24.02.2020
 */
public enum ExceptionCode {

    // general exception codes
    NOT_ALLOWED_TO_DO_ACTION(1000),
    ENTITY_NOT_FOUND_IN_DB(1001),
    ENTITY_ALREADY_EXISTS_IN_DB(1002),

    // user
    USERNAME_ALREADY_IN_USE(2000),

    ;

    private Integer code;

    ExceptionCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
