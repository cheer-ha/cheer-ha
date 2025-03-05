package com.project.cheerha.domain.history.service;

import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.common.exception.data.NotFoundException;
import com.project.cheerha.domain.history.entity.History;
import com.project.cheerha.domain.history.repository.HistoryRepository;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class historyNotificationDataFetchingSchedulerTest {

    @InjectMocks
    private HistoryScheduler historyScheduler;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private UserFindByService userFindByService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @Test
    void saveSearchHistoryToDb_정상적으로_동작할_경우() {
        // given
        String key = "user:1:search_history";
        Set<String> keys = Set.of(key);
        Set<String> searchTerms = Set.of("Redis", "Java");

        when(redisTemplate.keys("user:*:search_history")).thenReturn(keys);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(zSetOperations.range(key, 0, -1)).thenReturn(searchTerms);

        User mockUser = User.toEntity(
            "test@example.com",
            "testUser",
            25,
            3,
            "password123"
        );
        when(userFindByService.findById(1L)).thenReturn(mockUser);

        Set<String> existingSearchTermSet = Set.of("Java");
        when(historyRepository.findNamesByUserId(1L)).thenReturn(existingSearchTermSet);

        // when
        historyScheduler.saveSearchHistoryToDb();

        // then
        verify(historyRepository,times(1)).saveAll(anyList());
    }

    @Test
    void saveSearchHistoryToDb_저장할_검색어가_없을_경우() {
        // given
        String key = "user:1:search_history";
        Set<String> keys = Set.of(key);

        when(redisTemplate.keys("user:*:search_history")).thenReturn(keys);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(zSetOperations.range(key, 0, -1)).thenReturn(Collections.emptySet());

        // when
        historyScheduler.saveSearchHistoryToDb();

        // then
        verify(historyRepository, never()).save(any(History.class));
    }

    @Test
    void saveSearchHistoryToDb_유저가_없을_경우_예외_발생() {
        // given
        String key = "user:99:search_history";
        Set<String> keys = Set.of(key);

        when(redisTemplate.keys("user:*:search_history")).thenReturn(keys);
        when(userFindByService.findById(99L)).thenThrow(new NotFoundException(DataErrorCode.USER_NOT_FOUND));

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> historyScheduler.saveSearchHistoryToDb());
        assertEquals(DataErrorCode.USER_NOT_FOUND.getMessage(), exception.getMessage());
        verify(historyRepository, never()).save(any(History.class));
    }
}
