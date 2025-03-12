package com.project.cheerha.redis.count;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.repository.KeyValueRepository;
import com.project.cheerha.domain.user.service.CheckDailyEmailCount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    KeyValueRepository keyValueRepository;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String OPERATION_KEY = "password_reset";
    private static final String TODAY = LocalDate.now().toString();
    private static final String REDIS_KEY = "daily_email_count:" + OPERATION_KEY + ":" + TEST_EMAIL + ":" + TODAY;

    @Test
    void incrementDailyLimit_첫번째요청_카운트_1증가_만료시간설정() {
        when(keyValueRepository.getValue(REDIS_KEY)).thenReturn(null);

        assertDoesNotThrow(() -> checkDailyEmailCount.incrementDailyLimit(TEST_EMAIL, OPERATION_KEY));

        verify(keyValueRepository, times(1)).incrementValue(REDIS_KEY);
        verify(keyValueRepository, times(1)).expireValue(eq(REDIS_KEY), anyLong(), eq(TimeUnit.SECONDS));
    }

    @Test
    void incrementDailyLimit_네번째요청_예외발생() {
        when(keyValueRepository.getValue(REDIS_KEY)).thenReturn("3");

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> checkDailyEmailCount.incrementDailyLimit(TEST_EMAIL, OPERATION_KEY));

        assertEquals("일일 이메일 발송 횟수가 초과되었습니다.", exception.getMessage());

        verify(keyValueRepository, never()).incrementValue(REDIS_KEY);
        verify(keyValueRepository, never()).expireValue(anyString(), anyLong(), any());
    }

    @Test
    void incrementDailyLimit_잘못된값_예외발생() {
        when(keyValueRepository.getValue(REDIS_KEY)).thenReturn("INVALID");

        assertThrows(NumberFormatException.class,
                () -> checkDailyEmailCount.incrementDailyLimit(TEST_EMAIL, OPERATION_KEY));

        verify(keyValueRepository, never()).incrementValue(REDIS_KEY);
        verify(keyValueRepository, never()).expireValue(anyString(), anyLong(), any());
    }

    @Test
    void incrementDailyLimit_첫요청일때만_expire_설정() {
        when(keyValueRepository.getValue(REDIS_KEY)).thenReturn(null);
        long expectedExpiration = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay()).getSeconds();

        checkDailyEmailCount.incrementDailyLimit(TEST_EMAIL, OPERATION_KEY);

        verify(keyValueRepository, times(1)).expireValue(eq(REDIS_KEY), eq(expectedExpiration), eq(TimeUnit.SECONDS));
    }

    @Test
    void incrementDailyLimit_두번째이후_expire_설정안됨() {
        when(keyValueRepository.getValue(REDIS_KEY)).thenReturn("1");

        checkDailyEmailCount.incrementDailyLimit(TEST_EMAIL, OPERATION_KEY);

        verify(keyValueRepository, never()).expireValue(anyString(), anyLong(), any());
    }
}
