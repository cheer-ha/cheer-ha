package com.project.cheerha.domain.history.service;

import com.project.cheerha.domain.history.entity.History;
import com.project.cheerha.domain.history.repository.HistoryRepository;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class HistoryScheduler {

    private final UserFindByService userFindByService;
    private final HistoryRepository historyRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Redis에 저장된 검색를 주기적으로 DB에 저장하는 스케줄러 메서드입니다.
     *
     * 30분마다 실행이 되며, 각 사용자의 검색어를 Redis에서 가져와서 DB에 저장합니다.
     * 이미 저장된 검색어는 중복 저장하지 않습니다.
     */
    @Scheduled(fixedRate = 1800000) // 30분마다 실행
    public void saveSearchHistoryToDb() {
        Set<String> keys = redisTemplate.keys("user:*:search_history");

        if (keys != null) {
            for (String key : keys) {
                Long userId = Long.parseLong(key.split(":")[1]);
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
