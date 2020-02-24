package com.jovana.exception;

/**
 * Created by jovana on 24.02.2020
 */
public abstract class TddBoutiqueApiException extends RuntimeException {
    protected TddBoutiqueApiException(String message) {
        super(message);
    }
}
