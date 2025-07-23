package com.example.security;

import com.example.model.User;
import com.example.service.ITokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenProvider implements ITokenService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access-token-expiration-ms}")
    private long accessTokenExpiration;
    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpiration;

    /**
     * Extracts the username from a JWT.
     * @param token The JWT.
     * @return The username (phone number).
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from a JWT.
     * @param token The JWT.
     * @param claimsResolver Function to resolve the claim.
     * @return The claim value.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates authentication tokens for a user.
     * @param user The user object.
     * @return A map containing the access and refresh tokens.
     */
    public Map<String, String> generateAuthTokens(User user) {
        String accessToken = generateToken(new HashMap<>(), user, accessTokenExpiration);
        String refreshToken = generateToken(new HashMap<>(), user, refreshTokenExpiration);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    /**
     * Generates a JWT.
     * @param extraClaims Additional claims to include.
     * @param userDetails The user details.
     * @param expiration The token expiration time in ms.
     * @return The generated JWT string.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        User user = (User) userDetails;
        extraClaims.put("userId", user.getId().toString());
        extraClaims.put("name", user.getName());
        return Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey())
            .compact();
    }

    /**
     * Validates a JWT.
     * @param token The JWT.
     * @param userDetails The user details to validate against.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }



    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}