package com.jovana.service.integration.auth;

import com.jovana.token.TokenBasedAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * Created by jovana on 17.03.2020
 */
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    private static final String FAKE_JWT_TOKEN = "fakeJwtToken";

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserDetails principal = userDetailsService.loadUserByUsername(customUser.username());

        Authentication authenticationRequest = new TokenBasedAuthentication(principal, FAKE_JWT_TOKEN);
        context.setAuthentication(authenticationRequest);
        return context;
    }

}
