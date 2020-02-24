package com.jovana.entity.user;

import com.jovana.exception.TddBoutiqueApiException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Created by jovana on 24.02.2020
 */
@ResponseStatus(NOT_FOUND)
public class NotAllowedException extends TddBoutiqueApiException {

    public NotAllowedException (String message) {
        super(message);
    }

}
