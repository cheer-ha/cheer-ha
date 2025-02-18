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

    /**
     * 사용자의 AccessToken 을 블랙리스트에 저장하는 메서드
     * @param token 현재 사용자의 AccessToken
     */
    public void addToBlackList(String token) {
        String blackPrefix = jwtSecurityProperties.getToken().getBlackListPrefix();
        long blackExpiration = jwtSecurityProperties.getToken().getBlackListExpiration();
        RBucket<String> bucket = redissonClient.getBucket(blackPrefix + token);
        bucket.set("blackList", blackExpiration, TimeUnit.MILLISECONDS);
    }

    /**
     * 블랙리스트에 등록된 토큰인지 검증하는 메서드
     * @param token 현재 사용자의 AccessToken
     * @return 블랙리스트에 들어있다면 true / 아니면 false
     */
    public boolean isBlackList(String token) {
        String blackPrefix = jwtSecurityProperties.getToken().getBlackListPrefix();
        return redissonClient.getBucket(blackPrefix + token).isExists();
    }
}
