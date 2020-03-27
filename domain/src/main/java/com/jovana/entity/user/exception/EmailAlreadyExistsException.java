package com.jovana.entity.user.exception;

import com.jovana.exception.ErrorCode;
import com.jovana.exception.ExceptionCode;
import com.jovana.exception.TddBoutiqueApiException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(CONFLICT)
@ErrorCode(ExceptionCode.USERNAME_ALREADY_EXISTS)
public class EmailAlreadyExistsException extends TddBoutiqueApiException {

    private final String email;

    public EmailAlreadyExistsException(String message, String email) {
        super(message);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
