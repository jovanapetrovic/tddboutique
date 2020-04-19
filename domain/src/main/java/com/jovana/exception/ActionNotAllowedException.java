package com.jovana.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Created by jovana on 18.03.2020
 */
@ResponseStatus(BAD_REQUEST)
@ErrorCode(ExceptionCode.NOT_ALLOWED_TO_DO_ACTION)
public class ActionNotAllowedException extends TddBoutiqueApiException {

    public ActionNotAllowedException(String message) {
        super(message);
    }

}
