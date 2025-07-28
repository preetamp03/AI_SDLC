package com.example.messaging.security;

import com.example.messaging.exception.TokenValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.access-token.expiration}")
    private long accessTokenExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    /**
     * Generates an access token for the given user ID.
     * @param userId The user's UUID.
     * @return A JWT access token string.
     */
    public String generateAccessToken(UUID userId) {
        return buildToken(new HashMap<>(), userId, accessTokenExpiration);
    }

    /**
     * Generates a refresh token for the given user ID.
     * @param userId The user's UUID.
     * @return A JWT refresh token string.
     */
    public String generateRefreshToken(UUID userId) {
        return buildToken(new HashMap<>(), userId, refreshTokenExpiration);
    }
    
    /**
     * Extracts the user ID (subject) from a JWT token.
     * @param token The JWT token.
     * @return The user's UUID.
     */
    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    /**
     * Validates if a token is not expired.
     * @param token The JWT token.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            throw new TokenValidationException("Invalid JWT token: " + e.getMessage());
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
             throw new TokenValidationException("Could not parse JWT claims: " + e.getMessage());
        }
    }

    private String buildToken(Map<String, Object> extraClaims, UUID userId, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userId.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}