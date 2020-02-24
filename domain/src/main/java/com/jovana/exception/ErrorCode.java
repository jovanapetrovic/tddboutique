package com.jovana.exception;

import java.lang.annotation.*;

/**
 * Marks an  exception class with the error code and reason that should be returned. The error code can be applied
 * to the HTTP response when the handler method is invoked, or whenever said exception is thrown.
 *
 * @author Valentin Ionita
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ErrorCode {

    /**
     * The error code to use.
     */
    ExceptionCode value();

}
