package com.jovana.exception;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jovana on 24.02.2020
 */
@ControllerAdvice
public class ExceptionHandlerResource {

    public static final String JAVAX_SERVLET_ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
    private static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            error.getValidationErrors().add(
                    new ValidationError(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getValidationErrors().add(
                    new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return error;
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handle(TddBoutiqueApiException ex, HttpServletRequest request) throws IllegalAccessException {

        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Map<String, Object> errorAttributes = new HashMap<>();

        errorAttributes.put("timestamp", new Date());

        Object path = getAttribute(requestAttributes, JAVAX_SERVLET_ERROR_REQUEST_URI);
        errorAttributes.put("path", (path != null ? path : request.getRequestURI()));

        Object exception = getAttribute(requestAttributes, ERROR_ATTRIBUTE);
        if (exception != null) {
            errorAttributes.put("exception", exception.getClass().getName());
            errorAttributes.put("message", ((Exception) exception).getMessage());
        } else {
            errorAttributes.put("exception", ex.getClass().getName());
            errorAttributes.put("message", ex.getMessage());
        }

        Field[] declaredFields = ex.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            errorAttributes.put(field.getName(), field.get(ex));
        }

        HttpStatus statusCode = getStatusCode(ex);
        errorAttributes.put("status", statusCode.value());

        errorAttributes.put("error", getErrorCode(ex).value().getCode());

        return new ResponseEntity<>(errorAttributes, statusCode);
    }

    private HttpStatus getStatusCode(TddBoutiqueApiException ex) {
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.value();
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private ErrorCode getErrorCode(TddBoutiqueApiException ex) {
        return ex.getClass().getAnnotation(ErrorCode.class);
    }

    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }
}
