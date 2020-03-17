package com.jovana.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by jovana on 24.02.2020
 */
public class TokenBasedAuthentication extends AbstractAuthenticationToken {

    private String token;
    private final UserDetails principal;

    public TokenBasedAuthentication(UserDetails principal, String token) {
        super(principal.getAuthorities());
        this.principal = principal;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public UserDetails getPrincipal() {
        return principal;
    }
}
