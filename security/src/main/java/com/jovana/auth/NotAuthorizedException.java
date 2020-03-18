package com.jovana.auth;

import com.jovana.exception.ErrorCode;
import com.jovana.exception.TddBoutiqueApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.jovana.exception.ExceptionCode.BAD_CREDENTIALS;

/**
 * Created by Jovana on 4/01/2018.
 */
@ErrorCode(BAD_CREDENTIALS)
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthorizedException extends TddBoutiqueApiException {

    public NotAuthorizedException(String message) {
        super(message);
    }
}
