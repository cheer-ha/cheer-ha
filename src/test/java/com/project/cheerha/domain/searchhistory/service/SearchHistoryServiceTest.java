package com.project.cheerha.domain.searchhistory.service;

import com.project.cheerha.domain.searchhistory.entity.SearchHistory;
import com.project.cheerha.domain.searchhistory.repository.SearchHistoryRepository;
import com.project.cheerha.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchHistoryServiceTest {

    @InjectMocks
    private SearchHistoryService searchHistoryService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @Mock
    private SearchHistoryRepository searchHistoryRepository;

    private static final long EXPIRATION_TIME = 3600;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
    }

    @Test
    @DisplayName("검색어가 Redis에 정상적으로 저장")
    void Redis에_검색어_정상적으로_저장() {
        // given
        Long userId = 1L;
        String searchTerm = "Spring";
        String key = "user:" + userId + ":search_history";

        // when
        searchHistoryService.saveSearchTerm(userId, searchTerm);

        // then
        verify(zSetOperations, times(1)).add(eq(key), eq(searchTerm), anyDouble());
        verify(redisTemplate, times(1)).expire(eq(key), eq(EXPIRATION_TIME), eq(TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("검색어 개수가 MAX_SEARCH_HISOTRY_SIZE를 초과하면 가장 오래된 검색어 삭제")
    void Redis에서_가장_오래된_검색어_삭제() {
        //given
        Long userId = 2L;
        String searchTerm = "Redis";
        String key = "user:" + userId + ":search_history";

        when(zSetOperations.zCard(key)).thenReturn(11L);

        // when
        searchHistoryService.saveSearchTerm(userId, searchTerm);

        // then
        verify(zSetOperations, times(1)).add(eq(key), eq(searchTerm), anyDouble());
        verify(zSetOperations, times(1)).removeRange(eq(key),eq(0L), eq(0L));
        verify(redisTemplate, times(1)).expire(eq(key), eq(EXPIRATION_TIME), eq(TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("Redis에 검색 기록이 존재하면 해당 데이터를 반환")
    void Redis에_검색_기록_존재하면_데이터_반환() {
        // given
        Long userId = 1L;
        String key = "user:" + userId + ":search_history";
        Set<String> searchTermSet = new LinkedHashSet<>(List.of("Java", "Spring"));

        when(zSetOperations.reverseRange(key, 0, 9)).thenReturn(searchTermSet);

        // when
        List<String> searchTermList = searchHistoryService.getRecentSearchTerms(userId);

        // then
        assertThat(searchTermList).containsExactly("Java", "Spring");

        verify(searchHistoryRepository, never()).findTop10ByUserIdOrderByCreatedAtDesc(anyLong());
    }

    @Test
    @DisplayName("Redis에 검색 기록이 없으면 DB에서 데이터를 조회한 후 Redis에 저장하고 반환")
    void Redis에_검색_기록이_없으면_DB에서_데이터_조회하고_Redis에_저장_후_반환() {
        // given
        Long userId = 2L;
        String key = "user:" + userId + ":search_history";
        User mockUser = User.toEntity("test@gmail.com", "사용자1", 27, 1, "password123");

        when(zSetOperations.reverseRange(key, 0, 9)).thenReturn(Collections.emptySet());

        SearchHistory history1 = SearchHistory.toEntity(mockUser, "Elasticsearch");
        SearchHistory history2 = SearchHistory.toEntity(mockUser, "Kafka");

        ReflectionTestUtils.setField(history1, "createdAt", LocalDateTime.now().minusMinutes(10));
        ReflectionTestUtils.setField(history2, "createdAt", LocalDateTime.now().minusMinutes(5));

        List<SearchHistory> searchTermListInDatabase = List.of(history1, history2);

        when(searchHistoryRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId)).thenReturn(searchTermListInDatabase);

        // when
        List<String> resultList = searchHistoryService.getRecentSearchTerms(userId);

        //then
        assertThat(resultList).containsExactly("Kafka", "Elasticsearch");

        verify(searchHistoryRepository, times(1)).findTop10ByUserIdOrderByCreatedAtDesc(userId);

        verify(zSetOperations, times(1)).add(eq(key), eq("Elasticsearch"), anyDouble());
        verify(zSetOperations, times(1)).add(eq(key), eq("Kafka"), anyDouble());
    }
}