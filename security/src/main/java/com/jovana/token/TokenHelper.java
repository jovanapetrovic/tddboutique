package com.jovana.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Collection;
import java.util.Date;

/**
 * Created by jovana on 24.02.2020
 */
@Component
public class TokenHelper {

    @Value("${app.name}")
    private String appName;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiresIn}")
    private int expiresIn;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final String CUSTOM_CLAIM_KEY = "tddboutiqueapi";
    private static final String CUSTOM_CLAIM_VALUE = "tddboutique#2020";

    /**
     * This method will get the Subject Claim from the passed token,
     * which should keep the username (email in our case).
     * @param token
     * @return String username
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * This method will get the "role" Claim from the passed token,
     * which should keep the role.
     * @param token
     * @return String role
     */
    public String getRoleFromToken(String token) {
        String role;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            role = claims.get("role").toString();
        } catch (Exception e) {
            role = null;
        }
        return role;
    }

    /**
     * This method will generate a JWT token. JWT token consists of a Header, Payload and Signature.
     * Header contains the type of the token (JWT) and a hashing algorithm being used (HS512).
     * Payload contains Claims (registered and custom claims). Signature takes the encoded header,
     * the encoded payload, a secret and the algorithm specified in the header.
     * @param username
     * @return String token
     */
    public String generateToken(String username, Collection<GrantedAuthority> authorities) {

        String role = authorities.iterator().next().getAuthority().toString();

        return Jwts.builder()
                .setIssuer(appName)
                .setSubject(username)
                .claim(CUSTOM_CLAIM_KEY, CUSTOM_CLAIM_VALUE)    // set a custom check claim
                .claim("role", role)    // add user's role to claims
                .setIssuedAt(generateCurrentDate())
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, secret)
                .compact();
    }

    /**
     * This method will try to parse JWT from the passed token string. This will also validate the token.
     * The parser will throw an exception if token is not valid for any reason.
     * Exception that can be thrown are ExpiredJwtException, MalformedJwtException, SignatureException.
     * Follow the Jwts class for more info. If any exception occurs, Claims from the JWT payload will be null.
     * Anyway Jwts.parser() uses the "secret" key to verify the authenticity of the token and decode it.
     * That is the same key which we use above to encode a valid JWT when we generate a token.
     *
     * @param token
     * @return Claims
     */
    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()  // Parser will throw an exception if token is not a signed JWS (as expected)
                    .setSigningKey(this.secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * This method will try to validate token by using getClaimsFromToken() method.
     * If any exception occurs, Claims from the JWT payload will be null and the token won't be validated.
     * Otherwise, another check is done: if the ISSUER and CUSTOM_CLAIM_VALUE from the Claims are equal
     * to those that are encoded when JWT is generated and signed with SIGNATURE_ALGORITHM and "secret".
     * For more info, read the method's description above.
     * @param token
     * @return boolean (is token valid or not)
     */
    public boolean validateToken(String token) {
        Claims claims = this.getClaimsFromToken(token);
        return (claims != null) && (appName.equals(claims.getIssuer()) && CUSTOM_CLAIM_VALUE.equals(claims.get(CUSTOM_CLAIM_KEY)));
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    private long getCurrentTimeMillis() {
        return Instant.now().toEpochMilli();
    }

    private Date generateCurrentDate() {
        return new Date(getCurrentTimeMillis());
    }

    private Date generateExpirationDate() {
        return new Date(getCurrentTimeMillis() + this.expiresIn * 1000);
    }
}
