package com.project.cheerha.common.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisViewCountManager {

    private final RedisTemplate<String, String> redisTemplate;

    // 조회수 키의 기본 Prefix
    private static final String VIEW_COUNT_KEY_PREFIX = "viewCount:";

    /**
     * 특정 채용공고의 조회수를 증가시키는 메서드입니다.
     * @param jobOpeningId 조회수를 증가시킬 채용공고 ID
     */
    public void increaseViewCount(Long jobOpeningId) {
        String key = VIEW_COUNT_KEY_PREFIX + jobOpeningId; // Redis 키 생성
        redisTemplate.opsForValue().increment(key); //
        log.info("JobOpeningID : {} 현재 조회 수 : {}", jobOpeningId, redisTemplate.opsForValue().get(key)); //증가된 조회수 확인
    }

    /**
     * 채용공고의 조회수를 가져오는 메서드입니다.
     * @return 현재 Redis에 저장된 조회수 중 0보다 큰 값 전부
     */
    public Map<Long, Integer> getViewCount() {
        Set<String> keys = redisTemplate.keys(VIEW_COUNT_KEY_PREFIX + "*"); // 모든 조회수 키 찾기
        Map<Long, Integer> viewCounts = new HashMap<>();

        for (String key : keys) {
            String count = redisTemplate.opsForValue().get(key);
            Long jobOpeningId = Long.parseLong(key.replace(VIEW_COUNT_KEY_PREFIX, ""));
            viewCounts.put(jobOpeningId, count == null ? 0 : Integer.parseInt(count));
        }

        return viewCounts;
    }
}

