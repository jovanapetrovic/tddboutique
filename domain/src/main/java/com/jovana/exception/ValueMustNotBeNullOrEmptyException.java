package com.jovana.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * Created by jovana on 31.03.2020
 */
@ResponseStatus(CONFLICT)
@ErrorCode(ExceptionCode.VALUE_MUST_NOT_BE_EMPTY)
public class ValueMustNotBeNullOrEmptyException extends TddBoutiqueApiException {

    private final String valueName;

    public ValueMustNotBeNullOrEmptyException(String valueName, String message) {
        super(message);
        this.valueName = valueName;
    }

}
