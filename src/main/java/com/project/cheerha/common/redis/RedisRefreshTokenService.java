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

    /**
     * RefreshToken 을 redis 저장소에 삽입하는 메서드
     * @param userId 현재 사용자의 식별자
     * @param refreshToken AuthService 에서 만들어진 현재 사용자의 refreshToken
     */
    public void createRefreshToken(Long userId, String refreshToken) {
        long expiration = jwtSecurityProperties.token().refreshExpiration();
        String key = getKey(userId);
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(refreshToken, expiration, TimeUnit.MILLISECONDS);
    }

    /**
     * redis 저장소에서 RefreshToken 을 가져오는 메서드
     * @param userId 현재 사용자의 식별자
     * @return 토큰이 존재한다면 RefreshToken 반환, 아닐 시 빈 값 반환
     */
    public String getRefreshToken(Long userId) {
        String key = getKey(userId);
        String token = redissonClient.<String>getBucket(key).get();
        return token != null ? token : "";
    }

    /**
     * 사용자의 식별자로 RefreshToken 을 삭제하는 메서드
     * @param userId 현재 사용자의 식별자
     */
    public void deleteRefreshToken(Long userId) {
        String key = getKey(userId);
        redissonClient.getBucket(key).delete();
    }

    private String getKey(Long userId) {
        String prefix = jwtSecurityProperties.token().refreshPrefix();
        return prefix + userId;
    }
}


