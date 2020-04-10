package com.jovana.entity.product.exception;

import com.jovana.exception.ErrorCode;
import com.jovana.exception.ExceptionCode;
import com.jovana.exception.TddBoutiqueApiException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * Created by jovana on 11.04.2020
 */
@ResponseStatus(CONFLICT)
@ErrorCode(ExceptionCode.PRODUCT_ALREADY_EXISTS)
public class ProductNameAlreadyExistsException extends TddBoutiqueApiException {

    private final String name;

    public ProductNameAlreadyExistsException(String name, String message) {
        super(message);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
