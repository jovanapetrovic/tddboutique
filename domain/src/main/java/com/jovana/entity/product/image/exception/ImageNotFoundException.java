package com.jovana.entity.product.image.exception;

import com.jovana.exception.ErrorCode;
import com.jovana.exception.ExceptionCode;
import com.jovana.exception.TddBoutiqueApiException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Created by jovana on 11.04.2020
 */
@ResponseStatus(NOT_FOUND)
@ErrorCode(ExceptionCode.IMAGE_NOT_FOUND_EXCEPTION)
public class ImageNotFoundException extends TddBoutiqueApiException {

    public ImageNotFoundException(String message) {
        super(message);
    }

}
