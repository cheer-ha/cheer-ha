package com.project.cheerha.common.util;

import com.project.cheerha.common.properties.JwtSecurityProperties;

import com.project.cheerha.domain.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtSecurityProperties securityProperties;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /**
     * 어플리케이션 초기화 시 JWT secret key 를 초기화하는 메서드
     * Base64 인코딩된 키를 디코딩해 HMAC-SHA256 에 사용
     */
    @PostConstruct
    public void init() {
        String secretKey = securityProperties.getSecret().getKey();
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

    /**
     * 사용자 정보를 포함한 JWT AccessToken 생성
     * @param userId 현재 사용자의 식별자
     * @param role 현재 사용자의 권한정보
     * @return 생성된 AccessToken 문자열(prefix, ttl 포함)
     */
    public String createToken(Long userId, Role role) {
        String payload = userId + ":" + role;
        String encryptedPayload = Aes256Util.encrypt(payload);
        return generateJwt(encryptedPayload,
            securityProperties.getToken().getPrefix(),
            securityProperties.getToken().getExpiration());
    }

    /**
     * Jwt RefreshToken 생성(보안 상의 이유로 userId 만 포함)
     * @param userId 현재 사용자의 식별자
     * @return 생성된 RefreshToken 문자열(prefix, ttl 포함)
     */
    public String createRefreshToken(Long userId) {
        String payload = String.valueOf(userId);
        String encryptedPayload = Aes256Util.encrypt(payload);
        return generateJwt(encryptedPayload,
            securityProperties.getToken().getRefreshPrefix(),
            securityProperties.getToken().getRefreshExpiration());
    }

    /**
     * Jwt token 문자열에서 prefix 를 제거하여 순수 토큰 반환
     * @param token prefix 가 포함된 token
     * @return prefix 가 제거된 순수 token
     * @throws IllegalArgumentException 유효하지 않은 토큰일 경우
     */
    public String substringToken(String token) {
        String prefix = securityProperties.getToken().getPrefix();
        String refreshPrefix = securityProperties.getToken().getRefreshPrefix();
        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException("Token must not be null or empty");
        }
        if (!(token.startsWith(prefix) || token.startsWith(refreshPrefix))) {
            throw new IllegalArgumentException("Token does not start with a valid prefix");
        }
        if (token.startsWith(prefix)) {
            return token.substring(prefix.length()).trim();
        }
        if (token.startsWith(refreshPrefix)) {
            return token.substring(refreshPrefix.length()).trim();
        }
        throw new IllegalArgumentException("Not Found Token");
    }

    /**
     * JWT 를 파싱하여 Claims(토큰의 본문) 추출
     * AES256 복호화를 수행합니다
     * @param token prefix 가 제거된 순수 token
     * @return Claims 객체(토큰의 본문)
     */
    public Claims extractClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            String decryptedSubject = Aes256Util.decrypt(claims.getSubject());
            claims.setSubject(decryptedSubject);
            return claims;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid or expired JWT token");
        }
    }

    /**
     * JWT 를 생성하는 내부 메서드
     * @param encryptedPayload AES256 암호화 된 유저 정보
     * @param prefix 토큰 접두어(Access 와 Refresh 가 다름)
     * @param expiration 토큰 만료 시간(밀리초 단위)
     * @return 생성된 prefix 포함 JWT token
     */
    private String generateJwt(String encryptedPayload, String prefix, long expiration) {
        Date now = new Date();
        JwtBuilder jwtBuilder = Jwts.builder()
            .setSubject(encryptedPayload)
            .setExpiration(new Date(now.getTime() + expiration))
            .setIssuedAt(now)
            .signWith(key, signatureAlgorithm);

        return prefix + jwtBuilder.compact();
    }
}
