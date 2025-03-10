package com.project.cheerha.common.redis.viewcount;

import com.project.cheerha.common.dto.ViewCountResponseDto;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

        // 키가 없으면 기본값 0 설정
        if (redisTemplate.opsForValue().get(key) == null) {
            redisTemplate.opsForValue().set(key, "0", Duration.ofMinutes(30));  // 30분 TTL 설정
        }

        redisTemplate.opsForValue().increment(key);
    }

    /**
     * viewCount 단일 값
     * @param jobOpeningId
     * @return 채용공고 id에 해당하는 viewCount 값
     */
    public long getViewCount(Long jobOpeningId) {
        String key = VIEW_COUNT_KEY_PREFIX + jobOpeningId;
        String count = redisTemplate.opsForValue().get(key);
        return count == null ? 0 : Long.parseLong(count);
    }

    public void resetViewCount(Long jobOpeningId) {
        String key = VIEW_COUNT_KEY_PREFIX + jobOpeningId;
        redisTemplate.delete(key);
    }

    /**
     * 채용공고의 조회수를 전체 가져오는 메서드입니다.
     * 동기화를 위한 메서드
     * @return 현재 Redis에 저장된 조회수 전부
     */
    public List<ViewCountResponseDto> findAllViewCount() {
        Set<String> keys = redisTemplate.keys(VIEW_COUNT_KEY_PREFIX + "*"); // 모든 조회수 키 찾기
        List<ViewCountResponseDto> result = new ArrayList<>();

        for(String key : keys) {
            Long jobOpeningId = Long.parseLong(key.replace(VIEW_COUNT_KEY_PREFIX, ""));
            Long count = Long.valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get(key)));
            result.add(new ViewCountResponseDto(jobOpeningId, count));
        }

        return result;
    }
}

