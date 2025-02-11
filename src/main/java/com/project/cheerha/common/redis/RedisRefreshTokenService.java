package com.project.cheerha.common.redis;

import com.project.cheerha.common.properties.JwtSecurityProperties;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisRefreshTokenService {

    private final StringRedisTemplate redisTemplate;
    private final JwtSecurityProperties jwtSecurityProperties;

    public void createRefreshToken(Long userId, String refreshToken) {
        long expiration = jwtSecurityProperties.getToken().getRefreshExpiration();
        String key = getKey(userId);
        //set -> 원래 값을 수정함 -> 기존 refreshToken 덮어씌워짐
        redisTemplate.opsForValue().set(key, refreshToken, expiration, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(Long userId) {
        String key = getKey(userId);
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteRefreshToken(Long userId) {
        String key = getKey(userId);
        redisTemplate.delete(key);
    }

    private String getKey(Long userId) {
        String prefix = jwtSecurityProperties.getToken().getRefreshPrefix();
        return prefix + userId;
    }
}


