package com.project.cheerha.domain.history.service;

import com.project.cheerha.domain.history.dto.response.ReadHistoryResponseDto;
import com.project.cheerha.domain.history.entity.History;
import com.project.cheerha.domain.history.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final int MAX_HISTORY_SIZE = 10; // 최대 저장 개수
    private static final long EXPIRATION_TIME = 3600; // 1시간(초 단위)

    public List<ReadHistoryResponseDto> readAllHistories(Long userId) {
        List<History> historyList = historyRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        return historyList.stream()
            .map(ReadHistoryResponseDto::toDto)
            .toList();
    }

    public void saveSearchTerm(Long userId, String searchTerm) {
        String key = "user:" + userId + ":search_history";
        long timestamp = System.currentTimeMillis();

        redisTemplate.opsForZSet().add(key, searchTerm, timestamp);

        Long size = redisTemplate.opsForZSet().zCard(key);
        if (size != null && size > MAX_HISTORY_SIZE) {
            redisTemplate.opsForZSet().removeRange(key, 0, size - MAX_HISTORY_SIZE - 1);
        }

        redisTemplate.expire(key, EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    public List<String> getRecentSearchTerms(Long userId) {
        String key = "user:" + userId + ":search_history";

        Set<String> searchTerms = redisTemplate.opsForZSet().reverseRange(key, 0, 9);

        return searchTerms != null ? searchTerms.stream().toList() : List.of();
    }

}
