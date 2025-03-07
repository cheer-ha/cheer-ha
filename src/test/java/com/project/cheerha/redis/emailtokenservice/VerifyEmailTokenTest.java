package com.project.cheerha.redis.emailtokenservice;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.redis.email.EmailTokenService;
import com.project.cheerha.common.redis.email.VerificationFailCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerifyEmailTokenTest {

    @InjectMocks
    EmailTokenService emailTokenService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    VerificationFailCount verificationFailCount;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TOKEN_PREFIX = "notification_email_verification_token";
    private static final String VALID_TOKEN = "123456";
    private static final String INVALID_TOKEN = "654321";

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void verifyEmailToken_성공() {
        when(valueOperations.get(eq(TOKEN_PREFIX + ":" + TEST_EMAIL))).thenReturn(VALID_TOKEN);

        assertDoesNotThrow(() -> emailTokenService.verifyEmailToken(TOKEN_PREFIX, TEST_EMAIL, VALID_TOKEN));

        verify(redisTemplate, times(1)).delete(eq(TOKEN_PREFIX + ":" + TEST_EMAIL));
    }

    @Test
    void verifyEmailToken_토큰없음_예외발생() {
        when(valueOperations.get(eq(TOKEN_PREFIX + ":" + TEST_EMAIL))).thenReturn(null);

        assertThrows(BadRequestException.class,
                () -> emailTokenService.verifyEmailToken(TOKEN_PREFIX, TEST_EMAIL, VALID_TOKEN));
    }

    @Test
    void verifyEmailToken_잘못된토큰_예외발생() {
        when(valueOperations.get(eq(TOKEN_PREFIX + ":" + TEST_EMAIL))).thenReturn(VALID_TOKEN);

        assertThrows(BadRequestException.class,
                () -> emailTokenService.verifyEmailToken(TOKEN_PREFIX, TEST_EMAIL, INVALID_TOKEN));

        verify(verificationFailCount, times(1)).incrementFailCount(TEST_EMAIL, TOKEN_PREFIX);
    }
}
