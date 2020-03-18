package com.jovana.token;

import com.jovana.auth.NotAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by jovana on 07.12.2017
 */
@Component
public class TokenHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenHelper.class);

    @Value("${app.name}")
    private String appName;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiresIn}")
    private int expiresIn;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final String CUSTOM_CLAIM_KEY = "tddboutiqueapi";
    private static final String CUSTOM_CLAIM_VALUE = "tddboutique#2020";
    private static final String CUSTOM_CLAIM_FINGERPRINT = "userFingerprint";

    /**
     * This method will get the Subject Claim from the passed token,
     * which should keep the username (email in our case).
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
     * This method will generate a JWT token. JWT token consists of a Header, Payload and Signature.
     * Header contains the type of the token (JWT) and a hashing algorithm being used (HS512).
     * Payload contains Claims (registered and custom claims). Signature takes the encoded header,
     * the encoded payload, a secret and the algorithm specified in the header.
     */
    public String generateToken(String username, String fingerPrint) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        return Jwts.builder()
                .setIssuer(appName)
                .setSubject(username)
                .claim(CUSTOM_CLAIM_KEY, CUSTOM_CLAIM_VALUE)    // set a custom check claim
                .claim(CUSTOM_CLAIM_FINGERPRINT, hashFingerPrint(fingerPrint)) //hash a finger print as claim
                .setIssuedAt(generateCurrentDate())
                .setNotBefore(generateCurrentDate())
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, secret)
                .compact();
    }

    private String hashFingerPrint(String fingerPrint) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // Compute a SHA256 hash of the fingerprint in order to store the fingerprint hash (instead of the raw value) in the token
        // to prevent an XSS to be able to read the fingerprint and set the expected cookie itself
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] userFingerprintDigest = digest.digest(fingerPrint.getBytes("utf-8"));
        return DatatypeConverter.printHexBinary(userFingerprintDigest);
    }

    /**
     * This method will try to parse JWT from the passed token string. This will also validate the token.
     * The parser will throw an exception if token is not valid for any reason.
     * Exception that can be thrown are ExpiredJwtException, MalformedJwtException, SignatureException.
     * Follow the Jwts class for more info. If any exception occurs, Claims from the JWT payload will be null.
     * Anyway Jwts.parser() uses the "secret" key to verify the authenticity of the token and decode it.
     * That is the same key which we use above to encode a valid JWT when we generate a token.
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()  // Parser will throw an exception if token is not a signed JWS (as expected)
                    .setSigningKey(this.secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // throw error otherwise user gets the "Anonymous" role and will be perceived as authenticated (found by uploading asset)
            throw new NotAuthorizedException("Unauthorized");
        }
        return claims;
    }

    /**
     * First check if token is already revoked and therefore, invalid.
     * This method will try to validate token by using getClaimsFromToken() method.
     * If any exception occurs, Claims from the JWT payload will be null and the token won't be validated.
     * Otherwise, another check is done: if the ISSUER and CUSTOM_CLAIM_VALUE from the Claims are equal
     * to those that are encoded when JWT is generated and signed with SIGNATURE_ALGORITHM and "secret".
     * For more info, read the method's description above.
     */
    public boolean validateToken(String token, String cookie) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Claims claims = this.getClaimsFromToken(token);
        return (claims != null) && (appName.equals(claims.getIssuer()) && CUSTOM_CLAIM_VALUE.equals(claims.get(CUSTOM_CLAIM_KEY)));
    }

    public String getFingerPrintFromCookie(HttpServletRequest request) {
        // Retrieve the user fingerprint from the dedicated cookie
        String userFingerprint;
        if (request.getCookies() != null && request.getCookies().length > 0) {
            List<Cookie> cookies = Arrays.stream(request.getCookies()).collect(Collectors.toList());
            Optional<Cookie> cookie = cookies.stream().filter(c -> "_Secure-Fgp".equals(c.getName())).findFirst();
            if (cookie.isPresent()) {
                userFingerprint = cookie.get().getValue();
                return userFingerprint;
            }
        }
        return null;
        // throw new NotAuthorizedException("Authorization not valid");
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
