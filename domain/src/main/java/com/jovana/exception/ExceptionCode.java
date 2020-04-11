package com.jovana.exception;

/**
 * Created by jovana on 24.02.2020
 */
public enum ExceptionCode {

    // general exception codes
    NOT_ALLOWED_TO_DO_ACTION(1000),
    BAD_CREDENTIALS(1001),
    ENTITY_NOT_FOUND_IN_DB(1002),
    ENTITY_ALREADY_EXISTS_IN_DB(1003),
    VALUE_MUST_NOT_BE_EMPTY(1004),

    // user
    PASSWORDS_DONT_MATCH(1100),
    USERNAME_ALREADY_IN_USE(1101),
    USERNAME_ALREADY_EXISTS(1102),

    // shipping address
    INVALID_PHONE_NUMBER(1200),

    // product
    PRODUCT_ALREADY_EXISTS(1300),

    IMAGE_STORAGE_EXCEPTION(1400),
    IMAGE_NOT_FOUND_EXCEPTION(1401),

    ;

    private Integer code;

    ExceptionCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
