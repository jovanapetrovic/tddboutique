package com.jovana.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jovana on 28.02.2020
 */
public class ValidationErrorResponse {

    private List<ValidationError> validationErrors = new ArrayList<>();

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

}
