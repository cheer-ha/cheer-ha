package com.project.cheerha.domain.searchhistory.scheduler;

import com.project.cheerha.domain.searchhistory.entity.SearchHistory;
import com.project.cheerha.domain.searchhistory.repository.SearchHistoryRepository;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchHistoryTaskHandlerTest {

    @InjectMocks
    private SearchHistoryTaskHandler searchHistoryTaskHandler;

    @Mock
    private UserFindByService userFindByService;

    @Mock
    private SearchHistoryRepository searchHistoryRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @Captor
    private ArgumentCaptor<List<SearchHistory>> searchHistoryCaptor;

    @BeforeEach
    void setup() {
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
    }

    @Test
    @DisplayName("Redis에 검색 기록이 존재하고 중복되지 않은 검색어가 있으면 DB에 저장")
    void Redis에_중복되지_않은_검색어_있으면_DB_저장() {
        // given
        Long userId = 1L;
        String key = "user:" + userId + ":search_history";
        User mockUser = User.toEntity("test@gmail.com", "사용자", 27, 1, "password123");

        Set<String> redisSearchTermSet = new HashSet<>(Set.of("Elasticsearch", "Docker"));
        Set<String> existinfSearchTermSet = new HashSet<>(Set.of("Elasticsearch"));

        when(redisTemplate.keys("user:*:search_history")).thenReturn(Set.of(key));
        when(userFindByService.findById(userId)).thenReturn(mockUser);
        when(zSetOperations.range(key, 0, -1)).thenReturn(redisSearchTermSet);
        when(searchHistoryRepository.findNamesByUserId(userId)).thenReturn(existinfSearchTermSet);

        // when
        searchHistoryTaskHandler.handle(Collections.emptyMap());

        // then
        verify(searchHistoryRepository, times(1)).saveAll(searchHistoryCaptor.capture());

        List<SearchHistory> capturedList = searchHistoryCaptor.getValue();

        assertThat(capturedList).hasSize(1);
        assertThat(capturedList.get(0).getName()).isEqualTo("Docker");
    }

    @Test
    @DisplayName("Redis에 검색 기록이 있지만 모두 중복된 경우 DB에 저장하지 않음")
    void Redis에_검색_기록이_있지만_중복된_경우_DB에_저장하지_않음() {
        // given
        Long userId = 2L;
        String key = "user:" + userId + ":search_history";
        User mockUser = User.toEntity("test2@gmail.com", "사용자2", 25, 3, "password123");

        Set<String> redisSearchTermSet = new HashSet<>(Set.of("Elasticsearch", "Spring"));
        Set<String> existingSearchTermSet = new HashSet<>(Set.of("Elasticsearch", "Spring"));

        when(redisTemplate.keys("user:*:search_history")).thenReturn(Set.of(key));
        when(userFindByService.findById(userId)).thenReturn(mockUser);
        when(zSetOperations.range(key, 0, -1)).thenReturn(redisSearchTermSet);
        when(searchHistoryRepository.findNamesByUserId(userId)).thenReturn(existingSearchTermSet);

        // when
        searchHistoryTaskHandler.handle(Collections.emptyMap());

        // then
        verify(searchHistoryRepository, never()).saveAll(any());
    }
}
