package com.project.cheerha.redis.emailtokenservice;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.common.redis.email.CheckDailyEmailCount;
import com.project.cheerha.common.redis.email.EmailTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaveTokenTest {

    @InjectMocks
    EmailTokenService emailTokenService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    CheckDailyEmailCount checkDailyEmailCount;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TOKEN_PREFIX = "notification_email_verification_token";

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void saveToken_성공() {
        doNothing().when(valueOperations).set(anyString(), anyString(), anyLong(), any());

        String token = emailTokenService.saveToken(TOKEN_PREFIX, TEST_EMAIL);

        assertNotNull(token);
        verify(valueOperations, times(1)).set(eq(TOKEN_PREFIX + ":" + TEST_EMAIL), anyString(), anyLong(), any());
        verify(checkDailyEmailCount, times(1)).incrementDailyLimit(TEST_EMAIL, TOKEN_PREFIX);
    }

    @Test
    void saveToken_하루_제한_초과() {
        doThrow(new BadRequestException(ClientErrorCode.DAILY_LIMIT_EXCEEDED))
                .when(checkDailyEmailCount).incrementDailyLimit(TEST_EMAIL, TOKEN_PREFIX);

        assertThrows(BadRequestException.class, () -> emailTokenService.saveToken(TOKEN_PREFIX, TEST_EMAIL));
    }
}
