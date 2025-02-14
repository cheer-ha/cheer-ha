package com.project.cheerha.common.redis;

import com.project.cheerha.common.properties.JwtSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisBlackListService {

    private final RedissonClient redissonClient;
    private final JwtSecurityProperties jwtSecurityProperties;

    //token 을 블랙리스트로 추가하고 ttl 설정
    public void addToBlackList(String token) {
        String blackPrefix = jwtSecurityProperties.getToken().getBlackListPrefix();
        long blackExpiration = jwtSecurityProperties.getToken().getBlackListExpiration();
        RBucket<String> bucket = redissonClient.getBucket(blackPrefix + token);
        bucket.set("blackList", blackExpiration, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String token) {
        String blackPrefix = jwtSecurityProperties.getToken().getBlackListPrefix();
        return redissonClient.getBucket(blackPrefix + token).isExists();
    }
}
