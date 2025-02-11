package com.project.cheerha.common.util;

import com.project.cheerha.common.properties.JwtSecurityProperties;
import com.project.cheerha.domain.user.entity.User.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtSecurityProperties securityProperties;
    public static final Set<String> expiredTokenSet = new HashSet<>(); //TODO: redis 로 옮겨야 됨

    private Key key;
    private final String secretKey = securityProperties.getSecret().getKey();
    private final String prefix = securityProperties.getToken().getPrefix();
    private final long tokenTime = securityProperties.getToken().getExpiration();
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        if (!StringUtils.hasText(secretKey)) {
            log.error("JWT secret key is null or empty");
            throw new IllegalArgumentException("JWT secret key must not be null or empty");
        }
        try {
            byte[] bytes = Base64.getDecoder().decode(secretKey);
            key = Keys.hmacShaKeyFor(bytes);
            log.info("JWT secret key initialized successfully");
        } catch (IllegalArgumentException e) {
            log.error("Failed to decode JWT secret key: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid JWT secret key");
        }
    }

    public String createToken(Long userId, String email, Role role) {
        Date date = new Date();
        return prefix + Jwts.builder().setSubject(String.valueOf(userId))
            .claim("email", email).claim("userRole", role)
            .setExpiration(new Date(date.getTime() + tokenTime)).setIssuedAt(date)
            .signWith(key, signatureAlgorithm).compact();
    }

    public String substringToken(String tokenValue) {
        if (!StringUtils.hasText(tokenValue)) {
            throw new IllegalArgumentException("Token must not be null or empty");
        }
        if (!tokenValue.startsWith(prefix)) {
            throw new IllegalArgumentException("Token does not start with Bearer");
        }
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(prefix)) {
            return tokenValue.substring(7);
        }
        throw new IllegalArgumentException("Not Found Token");
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("Failed to parse JWT token: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid or expired JWT token");
        }
    }
}
