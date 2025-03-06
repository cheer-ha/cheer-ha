package com.project.cheerha.domain.searchhistory.scheduler;

import com.project.cheerha.common.util.SchedulerLockUtil;
import com.project.cheerha.domain.searchhistory.entity.SearchHistory;
import com.project.cheerha.domain.searchhistory.repository.SearchHistoryRepository;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SearchHistoryScheduler {

    private final SchedulerLockUtil schedulerLockUtil;
    private final UserFindByService userFindByService;
    private final SearchHistoryRepository searchHistoryRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Redis에 저장된 검색를 주기적으로 DB에 저장하는 스케줄러 메서드입니다.
     *
     * 30분마다 실행이 되며, 각 사용자의 검색어를 Redis에서 가져와서 DB에 저장합니다.
     * 각 사용자의 기존 검색어를 조회하여 Redis에 저장된 검색어와 비교합니다.
     * 이미 저장된 검색어는 중복 저장하지 않습니다.
     */
    @Scheduled(fixedRate = 1800000) // 30분마다 실행
    public void saveSearchHistoryToDb() {
        schedulerLockUtil.lock("save_search_history_to_redis_lock");
        Set<String> keys = redisTemplate.keys("user:*:search_history");

        for (String key : keys) {
            Long userId = Long.parseLong(key.split(":")[1]);
            User user = userFindByService.findById(userId);

            Set<String> searchTermSet = redisTemplate.opsForZSet().range(key, 0, -1);

            if (searchTermSet != null && !searchTermSet.isEmpty()) {
                // 해당 유저의 기존 검색어 조회
                Set<String> existingSearchTermSet = searchHistoryRepository.findNamesByUserId(userId);

                // 중복되지 않은 검색어 필터링
                List<SearchHistory> searchHistoryList = searchTermSet.stream()
                    .filter(term -> !existingSearchTermSet.contains(term))
                    .map(term -> SearchHistory.toEntity(user, term))
                    .toList();

                // 새로운 검색어가 있다면 저장
                if (!searchHistoryList.isEmpty()) {
                    searchHistoryRepository.saveAll(searchHistoryList);
                }
            }
        }
    }
}
