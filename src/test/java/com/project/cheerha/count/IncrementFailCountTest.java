package com.project.cheerha.count;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.repository.KeyValueCommandRepository;
import com.project.cheerha.common.repository.KeyValueQueryRepository;
import com.project.cheerha.domain.user.service.VerificationFailCount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class IncrementFailCountTest {

    @InjectMocks
    VerificationFailCount verificationFailCount;

    @Mock
    KeyValueQueryRepository keyValueQueryRepository;

    @Mock
    KeyValueCommandRepository keyValueCommandRepository;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String OPERATION_KEY = "password_reset";
    private static final String REDIS_KEY = "verification_fail_count:" + OPERATION_KEY + ":" + TEST_EMAIL;

    @Test
    void incrementFailCount_첫번째실패_카운트_1증가_만료시간설정() {
        when(keyValueQueryRepository.getValue(REDIS_KEY)).thenReturn(null);

        assertDoesNotThrow(() -> verificationFailCount.incrementFailCount(TEST_EMAIL, OPERATION_KEY));

        verify(keyValueCommandRepository, times(1)).setValue(eq(REDIS_KEY), eq("1"), eq(5L), eq(TimeUnit.MINUTES));
    }

    @Test
    void incrementFailCount_다섯번째실패_예외발생() {
        when(keyValueQueryRepository.getValue(REDIS_KEY)).thenReturn("5");

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> verificationFailCount.incrementFailCount(TEST_EMAIL, OPERATION_KEY));

        assertEquals("이메일 토큰 인증 횟수가 초과되었습니다.", exception.getMessage());

        verify(keyValueCommandRepository, never()).setValue(eq(REDIS_KEY), anyString(), anyLong(), any());
    }

    @Test
    void incrementFailCount_잘못된값_예외발생() {
        when(keyValueQueryRepository.getValue(REDIS_KEY)).thenReturn("INVALID");

        assertThrows(NumberFormatException.class,
                () -> verificationFailCount.incrementFailCount(TEST_EMAIL, OPERATION_KEY));

        verify(keyValueCommandRepository, never()).setValue(eq(REDIS_KEY), anyString(), anyLong(), any());
    }

    @Test
    void incrementFailCount_첫실패일때만_expire_설정() {
        when(keyValueQueryRepository.getValue(REDIS_KEY)).thenReturn(null);

        verificationFailCount.incrementFailCount(TEST_EMAIL, OPERATION_KEY);

        verify(keyValueCommandRepository, times(1)).setValue(eq(REDIS_KEY), eq("1"), eq(5L), eq(TimeUnit.MINUTES));
    }

    @Test
    void incrementFailCount_두번째이후_expire_설정안됨() {
        when(keyValueQueryRepository.getValue(REDIS_KEY)).thenReturn("1");

        verificationFailCount.incrementFailCount(TEST_EMAIL, OPERATION_KEY);

        verify(keyValueCommandRepository, never()).expireValue(anyString(), anyLong(), any());
    }
}
