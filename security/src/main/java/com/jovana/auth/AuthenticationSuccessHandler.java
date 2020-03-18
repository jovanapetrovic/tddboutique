package com.jovana.auth;

import com.jovana.token.TokenHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

/**
 * Created by jovana on 24.02.2020
 */
@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationSuccessHandler.class);

    @Value("${jwt.expiresIn}")
    private int expiresIn;
    @Autowired
    private TokenHelper tokenHelper;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        clearAuthenticationAttributes(request);
        User user = (User) authentication.getPrincipal();

        String fingerprint = generateFingerPrint();
        // Added fingerprint to avoid Token sideJacking
        try {
            String jwt = tokenHelper.generateToken(user.getUsername(), fingerprint);
            UserTokenState userTokenState = new UserTokenState(jwt, expiresIn);
            String jwtResponse = objectMapper.writeValueAsString(userTokenState);
            response.setContentType("application/json");
            response.getWriter().write(jwtResponse);
            response.addHeader("Set-Cookie", createFingerPrintCookie(fingerprint));
        } catch (Exception e) {
            LOG.debug("Error occurred while writing jwtResponse to json. Error message: ", e.getMessage());
        }
    }

    private String generateFingerPrint() {
        SecureRandom secureRandom = new SecureRandom();
        // Generate a random string that will constitute the fingerprint for this user
        byte[] randomFgp = new byte[50];
        secureRandom.nextBytes(randomFgp);
        return DatatypeConverter.printHexBinary(randomFgp);
    }

    private String createFingerPrintCookie(String userFingerprint) {
        return "_Secure-Fgp=" + userFingerprint + "; path=/"; // Only for production --> HttpOnly; Secure  SameSite=Strict;
    }

    private class UserTokenState {
        private String accessToken;
        private int expires;

        public UserTokenState(String accessToken, int expires) {
            this.accessToken = accessToken;
            this.expires = expires;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public int getExpires() {
            return expires;
        }

        public void setExpires(int expire) {
            this.expires = expire;
        }
    }

}
