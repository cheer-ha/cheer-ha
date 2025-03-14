package com.project.cheerha.redis.viewcount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.project.cheerha.common.redis.viewcount.RedisViewCountManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
public class RedisViewCountManagerTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisViewCountManager redisViewCountManager;

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

        // RedisTemplate의 opsForValue() 호출 시, 미리 설정한 valueOperations Mock 객체를 반환하도록 설정
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // valueOperations.increment(key)를 호출하면 1을 반환하도록 설정 (조회수 증가)
        when(valueOperations.increment(key)).thenReturn(1L);

        // valueOperations.get(key)를 호출하면 "1"을 반환하도록 설정 (조회수 조회)
        when(valueOperations.get(key)).thenReturn("1");

        /**
         * when: 테스트 실행
         * - increaseViewCount(1L)를 호출하여 조회수를 증가시킵니다.
         * - getViewCount(1L)를 호출하여 증가된 조회수를 조회합니다.
         */
        redisViewCountManager.increaseViewCount(postId);
        Long result = redisViewCountManager.getViewCount(postId);

        //then
        assertThat(result).isEqualTo(1L);
    }
}
