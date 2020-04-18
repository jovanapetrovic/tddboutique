package com.jovana.entity.order.exception;

import com.jovana.exception.ErrorCode;
import com.jovana.exception.ExceptionCode;
import com.jovana.exception.TddBoutiqueApiException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Created by jovana on 11.04.2020
 */
@ResponseStatus(BAD_REQUEST)
@ErrorCode(ExceptionCode.INVALID_CART_SIZE)
public class InvalidCartSizeException extends TddBoutiqueApiException {

    private final Integer size;

    public InvalidCartSizeException(Integer size, String message) {
        super(message);
        this.size = size;
    }

    public Integer getSize() {
        return size;
    }

}
