package com.project.cheerha.domain.searchhistory.scheduler;

import com.project.cheerha.common.scheduler.TaskHandler;
import com.project.cheerha.domain.searchhistory.entity.SearchHistory;
import com.project.cheerha.domain.searchhistory.repository.SearchHistoryRepository;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SearchHistoryTaskHandler implements TaskHandler {

    private final UserFindByService userFindByService;
    private final SearchHistoryRepository searchHistoryRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String getTaskType() {
        return "saveSearchHistoryToDb";
    }

    @Override
    public void handle(Map<String, Object> payload) {
        Set<String> keys = redisTemplate.keys("user:*:search_history");
        if (!keys.isEmpty()) {
            for (String key : keys) {
                Long userId = Long.parseLong(key.split(":")[1]);
                User user = userFindByService.findById(userId);
                Set<String> searchTermSet = redisTemplate.opsForZSet().range(key, 0, -1);

                if (searchTermSet != null && !searchTermSet.isEmpty()) {
                    Set<String> existingSearchTermSet = searchHistoryRepository.findNamesByUserId(userId);
                    List<SearchHistory> searchHistoryList = searchTermSet.stream()
                            .filter(term -> !existingSearchTermSet.contains(term))
                            .map(term -> SearchHistory.toEntity(user, term))
                            .toList();
                    if (!searchHistoryList.isEmpty()) {
                        searchHistoryRepository.saveAll(searchHistoryList);
                    }
                }
            }
        }
    }

    @Override
    public long getScheduleIntervalMillis() {
        return 1800000L; //30분
    }

    @Override
    public Map<String, Object> getDefaultPayload() {
        return null;
    }
}
