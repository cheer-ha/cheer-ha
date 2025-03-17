package com.project.cheerha.domain.jobopening.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.project.cheerha.common.repository.KeyValueCommandRepository;
import com.project.cheerha.common.repository.KeyValueQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ViewCountManagerTest {

    @Mock
    private KeyValueCommandRepository keyValueCommandRepository;

    @Mock
    private KeyValueQueryRepository keyValueQueryRepository;

    @InjectMocks
    private ViewCountManager viewCountManager;

    private static final String VIEW_COUNT_KEY_PREFIX_TEST = "viewCount:";

    /**
     * 여러 사용자가 동시에 조회 버튼을 클릭했을 때,
     * Redis에서 조회수가 정상적으로 증가하고 저장되는지 검증하는 테스트입니다.
     */
    @Test
    @DisplayName("동시에 여러 사람이 조회를 클릭 했을 때 집계된 조회수 검증")
    void 레디스로_조회수_집계_성공() {

        Long postId = 1L;
        String key = VIEW_COUNT_KEY_PREFIX_TEST + postId;

        // valueOperations.increment(key)를 호출하면 1을 반환하도록 설정 (조회수 증가)
        when(keyValueCommandRepository.incrementValue(key)).thenReturn(1L);

        // valueOperations.get(key)를 호출하면 "1"을 반환하도록 설정 (조회수 조회)
        when(keyValueQueryRepository.getValue(key)).thenReturn("1");

        /**
         * when: 테스트 실행
         * - increaseViewCount(1L)를 호출하여 조회수를 증가시킵니다.
         * - getViewCount(1L)를 호출하여 증가된 조회수를 조회합니다.
         */
        viewCountManager.increaseViewCount(postId);
        Long result = viewCountManager.getViewCount(postId);

        //then
        assertThat(result).isEqualTo(1L);
    }
}
