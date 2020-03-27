package com.jovana.exception;

/**
 * Created by jovana on 28.02.2020
 */
public class ValidationError {

    private final String fieldName;
    private final String message;

    public ValidationError(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }

}
