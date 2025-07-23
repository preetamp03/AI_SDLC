package com.example.security;

import com.example.dto.LoginResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtTokenProvider implements ITokenService {

    private static final Logger log = LogManager.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    private SecretKey secretKey;
    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginResponseDto generateAuthTokens(UUID userId, String phoneNumber) {
        String accessToken = createToken(userId, phoneNumber, accessTokenExpirationMs);
        String refreshToken = createToken(userId, phoneNumber, refreshTokenExpirationMs);
        return new LoginResponseDto(accessToken, refreshToken);
    }
    
    /**
     * Creates a JWT token.
     * @param userId The user's ID.
     * @param phoneNumber The user's phone number.
     * @param expirationMs The expiration time in milliseconds.
     * @return The generated JWT string.
     */
    private String createToken(UUID userId, String phoneNumber, long expirationMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(userId.toString())
                .claim("phone", phoneNumber)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        String userId = claims.getSubject();
        // Here we use the UserDetailsService to load user details from the DB by phone number/ID
        // For simplicity, we create UserDetails on the fly. In a real app, this should hit the DB.
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", Collections.emptyList());
    }
    
    /**
     * Extracts the user ID from the JWT token.
     * @param token The JWT token.
     * @return The user ID as a UUID.
     */
    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        return UUID.fromString(claims.getSubject());
    }
}