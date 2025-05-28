package com.uoroot.sgi.infrastructure.security;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service for managing blacklisted JWT tokens.
 * Maintains a set of invalidated tokens and provides methods to check
 * whether a token is blacklisted.
 */
@Service
public class TokenBlacklistService {

    private final ConcurrentHashMap<String, Long> blacklistedTokens = new ConcurrentHashMap<>();
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    /**
     * Adds a token to the blacklist
     * @param token The JWT token to invalidate
     */
    public void blacklistToken(String token) {
        if (token != null && !token.isEmpty()) {
            long expiryTimestamp = System.currentTimeMillis() + jwtExpiration;
            blacklistedTokens.put(token, expiryTimestamp);
        }
    }
    
    
    /**
     * Checks if a token is blacklisted
     * @param token The JWT token to check
     * @return true if the token is blacklisted, false otherwise
     */
    public boolean isTokenBlacklisted(String token) {
        Long expiry = blacklistedTokens.get(token);
        return expiry != null && expiry > System.currentTimeMillis();
    }
    
    /**
     * Scheduled task to clean up expired tokens from the blacklist
     * Executes every hour
     */
    @Scheduled(fixedRate = 3600000) // Every hour
    public void cleanupBlacklist() {
        long now = System.currentTimeMillis();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue() <= now);
    }

}