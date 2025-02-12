package com.project.cheerha.common.redis;

import com.project.cheerha.common.properties.JwtSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisBlackListService {

    private final StringRedisTemplate redisTemplate;
    private final JwtSecurityProperties jwtSecurityProperties;

    //token 을 블랙리스트로 추가하고 ttl 설정
    public void addToBlackList(String token) {
        String blackPrefix = jwtSecurityProperties.getToken().getBlackListPrefix();
        long blackExpiration = jwtSecurityProperties.getToken().getBlackListExpiration();
        redisTemplate.opsForValue().set(blackPrefix + token, "blacklisted", Duration.ofMillis(blackExpiration));
    }

    public boolean isBlacklisted(String token) {
        String blackPrefix = jwtSecurityProperties.getToken().getBlackListPrefix();
        return Boolean.TRUE.equals(redisTemplate.hasKey(blackPrefix + token));
    }
}
