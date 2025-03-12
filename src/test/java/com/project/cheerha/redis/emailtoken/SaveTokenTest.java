package com.project.cheerha.redis.emailtoken;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.common.repository.KeyValueRepository;
import com.project.cheerha.domain.user.service.CheckDailyEmailCount;
import com.project.cheerha.domain.user.service.EmailTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaveTokenTest {

    @InjectMocks
    EmailTokenService emailTokenService;

    @Mock
    private KeyValueRepository keyValueRepository;

    @Mock
    CheckDailyEmailCount checkDailyEmailCount;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TOKEN_PREFIX = "notification_email_verification_token";

    @Test
    void saveToken_성공() {
        doNothing().when(keyValueRepository).setValue(anyString(), anyString(), anyLong(), any());

        String token = emailTokenService.saveToken(TOKEN_PREFIX, TEST_EMAIL);

        assertNotNull(token);
        verify(keyValueRepository, times(1)).setValue(eq(TOKEN_PREFIX + ":" + TEST_EMAIL), anyString(), anyLong(), any());
        verify(checkDailyEmailCount, times(1)).incrementDailyLimit(TEST_EMAIL, TOKEN_PREFIX);
    }

    @Test
    void saveToken_하루_제한_초과() {
        doThrow(new BadRequestException(ClientErrorCode.DAILY_LIMIT_EXCEEDED))
                .when(checkDailyEmailCount).incrementDailyLimit(TEST_EMAIL, TOKEN_PREFIX);

        assertThrows(BadRequestException.class, () -> emailTokenService.saveToken(TOKEN_PREFIX, TEST_EMAIL));
    }
}
