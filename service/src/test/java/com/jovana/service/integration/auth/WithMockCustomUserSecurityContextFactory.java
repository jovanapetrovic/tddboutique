package com.jovana.service.integration.auth;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jovana.auth.UserDetailsServiceImpl;
import com.jovana.entity.authority.Authority;
import com.jovana.entity.authority.AuthorityConstants;
import com.jovana.repositories.user.UserRepository;
import com.jovana.token.TokenBasedAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by jovana on 06.04.2020
 */
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    private static final String FAKE_JWT_TOKEN = "testITJwtToken";

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        Set<String> authoritiesArray = new HashSet<>(Arrays.asList(customUser.authorities()));
        Set<SimpleGrantedAuthority> authorities = authoritiesArray
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        UserDetails principal = new com.jovana.auth.UserDetails(
                Long.parseLong(customUser.userId()),
                customUser.username(),
                customUser.password(),
                authorities);
        Authentication auth = new TokenBasedAuthentication(principal, FAKE_JWT_TOKEN);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }
}
