package com.jovana.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Created by jovana on 18.03.2020
 *
 * A generic exception for the entities that already exist in db.
 */
@ResponseStatus(BAD_REQUEST)
@ErrorCode(ExceptionCode.ENTITY_ALREADY_EXISTS_IN_DB)
public class EntityAlreadyExistsException extends TddBoutiqueApiException {

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

}
