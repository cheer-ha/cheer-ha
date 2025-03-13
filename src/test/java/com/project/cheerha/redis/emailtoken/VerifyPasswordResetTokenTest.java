package com.project.cheerha.redis.emailtoken;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.repository.keyvalue.KeyValueCommandRepository;
import com.project.cheerha.common.repository.keyvalue.KeyValueQueryRepository;
import com.project.cheerha.domain.user.service.EmailTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerifyPasswordResetTokenTest {

    @InjectMocks
    private EmailTokenService emailTokenService;

    @Mock
    private KeyValueCommandRepository keyValueCommandRepository;

    @Mock
    private KeyValueQueryRepository keyValueQueryRepository;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String VALID_TOKEN = "123456";
    private static final String INVALID_TOKEN = "654321";
    private static final String REDIS_KEY = "password_verification:" + TEST_EMAIL;

    @Test
    void verifyPasswordResetToken_성공() {
        when(keyValueQueryRepository.getValue(REDIS_KEY)).thenReturn(VALID_TOKEN);

        assertDoesNotThrow(() -> emailTokenService.verifyPasswordResetToken(TEST_EMAIL, VALID_TOKEN));

        verify(keyValueCommandRepository, times(1)).removeValue(REDIS_KEY);
    }

    @Test
    void verifyPasswordResetToken_토큰없음_예외발생() {
        when(keyValueQueryRepository.getValue(REDIS_KEY)).thenReturn(null);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> emailTokenService.verifyPasswordResetToken(TEST_EMAIL, VALID_TOKEN));

        assertEquals("패스워드 변경용 토큰이 유효하지 않습니다.", exception.getMessage());
        verify(keyValueCommandRepository, times(1)).removeValue(REDIS_KEY);
    }

    @Test
    void verifyPasswordResetToken_잘못된토큰_예외발생() {
        when(keyValueQueryRepository.getValue(REDIS_KEY)).thenReturn(VALID_TOKEN);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> emailTokenService.verifyPasswordResetToken(TEST_EMAIL, INVALID_TOKEN));

        assertEquals("패스워드 변경용 토큰이 유효하지 않습니다.", exception.getMessage());
        verify(keyValueCommandRepository, times(1)).removeValue(REDIS_KEY);
    }
}
