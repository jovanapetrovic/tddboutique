package com.jovana.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Created by jovana on 24.02.2020
 *
 * A generic exception for the entities that are not found in db through repository methods.
 */
@ResponseStatus(BAD_REQUEST)
@ErrorCode(ExceptionCode.ENTITY_NOT_FOUND_IN_DB)
public class EntityNotFoundException extends TddBoutiqueApiException {

    public EntityNotFoundException(String message) {
        super(message);
    }

}
