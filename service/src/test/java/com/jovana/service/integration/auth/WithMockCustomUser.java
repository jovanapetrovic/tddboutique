package com.jovana.service.integration.auth;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jovana on 17.03.2020
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    /**
     * The username to be used. The default is test.
     */
    String username() default "test";

    /**
     * The password to be used. The default is test.
     */
    String password() default "test";

}