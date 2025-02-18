package com.project.cheerha.domain.history.service;

import com.project.cheerha.domain.history.entity.History;
import com.project.cheerha.domain.history.repository.HistoryRepository;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final UserFindByService userFindByService;
    private final RedisTemplate<String, String> redisTemplate;

    private static final int MAX_HISTORY_SIZE = 10; // 최대 저장 개수
    private static final long EXPIRATION_TIME = 3600; // 1시간(초 단위)

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
        if (searchTerms == null || searchTerms.isEmpty()) {
            List<String> dbSearchTerms = historyRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .sorted(Comparator.comparing(History::getCreatedAt))
                .map(History::getName)
                .collect(Collectors.toList());

            dbSearchTerms.forEach(term -> saveSearchTerm(userId, term));
            Collections.reverse(dbSearchTerms);

            return dbSearchTerms;
        }

        return new ArrayList<>(searchTerms);
    }

    @Scheduled(fixedRate = 1800000) // 30분마다 실행
    public void saveSearchHistoryToDb() {
        Set<String> keys = redisTemplate.keys("user:*:search_history");

        if (keys != null) {
            for (String key : keys) {
                Long userId = Long.parseLong(key.split(":")[1]); // "user:123:search_history" → userId 추출
                User user = userFindByService.findById(userId);

                Set<String> searchTerms = redisTemplate.opsForZSet().range(key, 0, -1);

                if (searchTerms != null) {
                    searchTerms.forEach(term -> {
                        if (!historyRepository.existsByUserIdAndName(userId, term)) {
                            historyRepository.save(History.toEntity(user, term));
                        }
                    });
                }
            }
        }
    }

}
