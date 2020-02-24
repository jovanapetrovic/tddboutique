package com.jovana.token;

import com.jovana.auth.AnonAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jovana on 24.02.2020
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Value("${jwt.header}")
    private String AUTH_HEADER;

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    UserDetailsService userDetailsService;

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String error = "";
        String authToken = getToken(request);

        if (authToken != null) {

            // Check the authenticity of the token
            if (!tokenHelper.validateToken(authToken)) {
                error = "ERROR: Validation of authenticity for provided token failed.";
            } else {
                // Get username from token
                String username = tokenHelper.getUsernameFromToken(authToken);

                if (username != null) {
                    // Get user
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Perform a role check
                    String role = tokenHelper.getRoleFromToken(authToken);
                    String authority = userDetails.getAuthorities().iterator().next().toString();

                    if (!role.equals(authority)) {
                        error = "ERROR: Role from token doesn't match the user's role from the db.";
                    } else {
                        // Create authentication
                        TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                        authentication.setToken(authToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    error = "ERROR: Username from token can't be found in DB.";
                }
            }
        } /* else {
            error = "Authentication failed because no Bearer token is provided."; // no authentication should be set
        } */

        if( !error.isEmpty()){
            LOG.error("Internal filtering threw an error => {}", error);
            SecurityContextHolder.getContext().setAuthentication(new AnonAuthentication()); // authenticated as ANONYMOUS, no authorities
        }

        chain.doFilter(request, response);
    }

}
