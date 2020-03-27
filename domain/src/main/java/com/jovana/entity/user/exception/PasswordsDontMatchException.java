package com.jovana.entity.user.exception;

import com.jovana.exception.ErrorCode;
import com.jovana.exception.ExceptionCode;
import com.jovana.exception.TddBoutiqueApiException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * Created by jovana on 27.03.2020
 */
@ResponseStatus(CONFLICT)
@ErrorCode(ExceptionCode.PASSWORDS_DONT_MATCH)
public class PasswordsDontMatchException extends TddBoutiqueApiException {

    public PasswordsDontMatchException(String message) {
        super(message);
    }

}
