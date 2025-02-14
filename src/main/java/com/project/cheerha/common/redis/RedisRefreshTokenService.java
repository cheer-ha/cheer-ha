package com.project.cheerha.common.redis;

import com.project.cheerha.common.properties.JwtSecurityProperties;

import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisRefreshTokenService {

    private final RedissonClient redissonClient;
    private final JwtSecurityProperties jwtSecurityProperties;

    public void createRefreshToken(Long userId, String refreshToken) {
        long expiration = jwtSecurityProperties.getToken().getRefreshExpiration();
        String key = getKey(userId);
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(refreshToken, expiration, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(Long userId) {
        String key = getKey(userId);
        String token = redissonClient.<String>getBucket(key).get();
        return token != null ? token : "";
    }

    public void deleteRefreshToken(Long userId) {
        String key = getKey(userId);
        redissonClient.getBucket(key).delete();
    }

    private String getKey(Long userId) {
        String prefix = jwtSecurityProperties.getToken().getRefreshPrefix();
        return prefix + userId;
    }
}


