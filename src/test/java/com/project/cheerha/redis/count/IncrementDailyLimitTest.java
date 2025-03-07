package com.project.cheerha.redis.count;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.redis.email.CheckDailyEmailCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class IncrementDailyLimitTest {

    @InjectMocks
    CheckDailyEmailCount checkDailyEmailCount;

    @Mock
    RedisTemplate<String, String> redisTemplate;

    @Mock
    ValueOperations<String, String> valueOperations;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String OPERATION_KEY = "password_reset";
    private static final String TODAY = LocalDate.now().toString();
    private static final String REDIS_KEY = "daily_email_count:" + OPERATION_KEY + ":" + TEST_EMAIL + ":" + TODAY;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void incrementDailyLimit_첫번째요청_카운트_1증가_만료시간설정() {
        when(valueOperations.get(REDIS_KEY)).thenReturn(null);

        assertDoesNotThrow(() -> checkDailyEmailCount.incrementDailyLimit(TEST_EMAIL, OPERATION_KEY));

        verify(valueOperations, times(1)).increment(REDIS_KEY);
        verify(redisTemplate, times(1)).expire(eq(REDIS_KEY), anyLong(), eq(TimeUnit.SECONDS));
    }

    @Test
    void incrementDailyLimit_네번째요청_예외발생() {
        when(valueOperations.get(REDIS_KEY)).thenReturn("3");

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> checkDailyEmailCount.incrementDailyLimit(TEST_EMAIL, OPERATION_KEY));

        assertEquals("일일 이메일 발송 횟수가 초과되었습니다.", exception.getMessage());

        verify(valueOperations, never()).increment(REDIS_KEY);
        verify(redisTemplate, never()).expire(anyString(), anyLong(), any());
    }

    @Test
    void incrementDailyLimit_잘못된값_예외발생() {
        when(valueOperations.get(REDIS_KEY)).thenReturn("INVALID");

        assertThrows(NumberFormatException.class,
                () -> checkDailyEmailCount.incrementDailyLimit(TEST_EMAIL, OPERATION_KEY));

        verify(valueOperations, never()).increment(REDIS_KEY);
        verify(redisTemplate, never()).expire(anyString(), anyLong(), any());
    }

    @Test
    void incrementDailyLimit_첫요청일때만_expire_설정() {
        when(valueOperations.get(REDIS_KEY)).thenReturn(null);
        long expectedExpiration = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay()).getSeconds();

        checkDailyEmailCount.incrementDailyLimit(TEST_EMAIL, OPERATION_KEY);

        verify(redisTemplate, times(1)).expire(eq(REDIS_KEY), eq(expectedExpiration), eq(TimeUnit.SECONDS));
    }

    @Test
    void incrementDailyLimit_두번째이후_expire_설정안됨() {
        when(valueOperations.get(REDIS_KEY)).thenReturn("1");

        checkDailyEmailCount.incrementDailyLimit(TEST_EMAIL, OPERATION_KEY);

        verify(redisTemplate, never()).expire(anyString(), anyLong(), any());
    }
}
