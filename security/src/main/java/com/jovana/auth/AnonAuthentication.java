package com.jovana.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Created by jovana on 24.02.2020
 */
public class AnonAuthentication extends AbstractAuthenticationToken {

    public AnonAuthentication() {
        super(null);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        return true;
    }

}
