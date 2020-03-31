package com.jovana.entity.shippingaddress.exception;

import com.jovana.exception.ErrorCode;
import com.jovana.exception.ExceptionCode;
import com.jovana.exception.TddBoutiqueApiException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Created by jovana on 31.03.2020
 */
@ResponseStatus(BAD_REQUEST)
@ErrorCode(ExceptionCode.INVALID_PHONE_NUMBER)
public class InvalidPhoneNumberException extends TddBoutiqueApiException {

    private final String phone;
    private final String failureReason;

    public InvalidPhoneNumberException(String phone, String failureReason, String message) {
        super(message);
        this.phone = phone;
        this.failureReason = failureReason;
    }

}
