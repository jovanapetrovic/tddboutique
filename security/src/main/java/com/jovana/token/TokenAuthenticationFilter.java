package com.jovana.token;

import com.jovana.auth.AnonAuthentication;
import com.jovana.auth.NotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jovana on 07.12.2017
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
    private static final String LOGOUT_REQUEST = "/api/logout";

    @Value("${jwt.header}")
    private String AUTH_HEADER;
    @Autowired
    private TokenHelper tokenHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;


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
            String cookie = tokenHelper.getFingerPrintFromCookie(request);
            // Check the authenticity of the token
            try {
                if (!tokenHelper.validateToken(authToken, cookie)) {
                    error = "ERROR: Validation of authenticity for provided token failed!";
                    throw new NotAuthorizedException("Your credentials are not correct");
                } else {
                    // Get username from token
                    String username = tokenHelper.getUsernameFromToken(authToken);

                    if (username != null) {
                        // Get user
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        // Create authentication
                        TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails, authToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        error = "ERROR: Username from token can't be found in DB!";
                        throw new NotAuthorizedException("Your credentials are not correct");
                    }
                }
            } catch (NoSuchAlgorithmException e) {
                LOG.error("No such Algorithm exception {}", e);
            }
        }

        if (!error.isEmpty()) {
            LOG.error("Internal filtering threw an error => {}", error);
            SecurityContextHolder.getContext().setAuthentication(new AnonAuthentication()); // authenticated as ANONYMOUS, no authorities
        }

        chain.doFilter(request, response);
    }

}
