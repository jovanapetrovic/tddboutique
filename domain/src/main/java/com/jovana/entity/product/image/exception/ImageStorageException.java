package com.jovana.entity.product.image.exception;

import com.jovana.exception.ErrorCode;
import com.jovana.exception.ExceptionCode;
import com.jovana.exception.TddBoutiqueApiException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Created by jovana on 11.04.2020
 */
@ResponseStatus(INTERNAL_SERVER_ERROR)
@ErrorCode(ExceptionCode.IMAGE_STORAGE_EXCEPTION)
public class ImageStorageException extends TddBoutiqueApiException {

    public ImageStorageException(String message) {
        super(message);
    }

}
