package com.jovana.service.integration.auth;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jovana on 06.04.2020
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String userId() default "2";

    String username() default "test";

    String password() default "123456";

    String[] authorities() default {"ROLE_USER"};

}
