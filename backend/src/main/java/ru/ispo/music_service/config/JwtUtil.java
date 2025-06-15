package ru.ispo.music_service.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private final String SECRET_KEY = "aX6+qZ7mRlWI5qk0G8ZJ3NxYRfYw1bT2zKjLpSvHtD0="; 
    private final long EXPIRATION_TIME = 864_000_000; 

    public String generateToken(UserDetails userDetails) {
        logger.info("Generating token for user: {}", userDetails.getUsername());
        logger.debug("User authorities: {}", userDetails.getAuthorities());
        
        String token = Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        
        logger.debug("Generated token: {}", token);
        return token;
    }

    public String extractUsername(String token) {
        try {
            String username = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getPayload()
                    .getSubject();
            logger.info("Extracted username from token: {}", username);
            return username;
        } catch (Exception e) {
            logger.error("Error extracting username from token: {}", e.getMessage());
            throw e;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            logger.info("Token validation successful");
            return true;
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
}