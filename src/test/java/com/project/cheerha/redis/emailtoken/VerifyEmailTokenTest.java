package com.project.cheerha.redis.emailtoken;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.repository.keyvalue.KeyValueCommandRepository;
import com.project.cheerha.common.repository.keyvalue.KeyValueQueryRepository;
import com.project.cheerha.domain.user.service.EmailTokenService;
import com.project.cheerha.domain.user.service.VerificationFailCount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerifyEmailTokenTest {

    @InjectMocks
    EmailTokenService emailTokenService;

    @Mock
    private KeyValueQueryRepository keyValueQueryRepository;

    @Mock
    private KeyValueCommandRepository keyValueCommandRepository;

    @Mock
    VerificationFailCount verificationFailCount;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TOKEN_PREFIX = "notification_email_verification_token";
    private static final String VALID_TOKEN = "123456";
    private static final String INVALID_TOKEN = "654321";

    @Test
    void verifyEmailToken_성공() {
        when(keyValueQueryRepository.getValue(eq(TOKEN_PREFIX + ":" + TEST_EMAIL))).thenReturn(VALID_TOKEN);

        assertDoesNotThrow(() -> emailTokenService.verifyEmailToken(TOKEN_PREFIX, TEST_EMAIL, VALID_TOKEN));

        verify(keyValueCommandRepository, times(1)).removeValue(eq(TOKEN_PREFIX + ":" + TEST_EMAIL));
    }

    @Test
    void verifyEmailToken_토큰없음_예외발생() {
        when(keyValueQueryRepository.getValue(eq(TOKEN_PREFIX + ":" + TEST_EMAIL))).thenReturn(null);

        assertThrows(BadRequestException.class,
                () -> emailTokenService.verifyEmailToken(TOKEN_PREFIX, TEST_EMAIL, VALID_TOKEN));
    }

    @Test
    void verifyEmailToken_잘못된토큰_예외발생() {
        when(keyValueQueryRepository.getValue(eq(TOKEN_PREFIX + ":" + TEST_EMAIL))).thenReturn(VALID_TOKEN);

        assertThrows(BadRequestException.class,
                () -> emailTokenService.verifyEmailToken(TOKEN_PREFIX, TEST_EMAIL, INVALID_TOKEN));

        verify(verificationFailCount, times(1)).incrementFailCount(TEST_EMAIL, TOKEN_PREFIX);
    }
}
