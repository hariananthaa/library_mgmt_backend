package com.hsk.library_mgmt_backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Service class for handling JWT (JSON Web Token) operations.
 * <p>
 * This service provides methods to generate, validate, and extract information from JWT tokens.
 * </p>
 */
@Service
public class JWTService {

    @Value("${jwt-secret-key}")
    private String SECRET_KEY;

    /**
     * Extracts the username from the JWT token.
     *
     * @param token the JWT token from which the username needs to be extracted.
     * @return the username contained in the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token the JWT token from which the claim needs to be extracted.
     * @param claimsResolver a function to extract the claim from the Claims object.
     * @param <T> the type of the claim to be extracted.
     * @return the extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token for the given user details and extra claims.
     *
     * @param userDetails the user details for which the token is to be generated.
     * @param claims additional claims to be included in the token.
     * @return the generated JWT token.
     */
    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        return generateToken(claims, userDetails);
    }

    /**
     * Generates a JWT token with extra claims and user details.
     *
     * @param extraClaims additional claims to be included in the token.
     * @param userDetails the user details for which the token is to be generated.
     * @return the generated JWT token.
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Validates the JWT token against the provided user details.
     *
     * @param token the JWT token to be validated.
     * @param userDetails the user details to check the token against.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token the JWT token to be checked.
     * @return true if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token from which the expiration date needs to be extracted.
     * @return the expiration date of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token from which claims need to be extracted.
     * @return the claims contained in the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retrieves the signing key used for JWT token signing.
     *
     * @return the SecretKey used for signing JWT tokens.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
