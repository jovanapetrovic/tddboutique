package com.jovana.entity.user.exception;

import com.jovana.exception.TddBoutiqueApiException;
import com.jovana.exception.ErrorCode;
import com.jovana.exception.ExceptionCode;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * Created by jovana on 24.02.2020
 */
@ResponseStatus(CONFLICT)
@ErrorCode(ExceptionCode.USERNAME_ALREADY_IN_USE)
public class UsernameAlreadyExistsException extends TddBoutiqueApiException {

    private final String login;

    public UsernameAlreadyExistsException(String login, String message) {
        super(message);
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
