package com.project.cheerha.redis.emailtokenservice;

import com.project.cheerha.common.redis.email.EmailTokenService;
import com.project.cheerha.common.util.SecureRandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaveSecureTokenTest {

    @InjectMocks
    EmailTokenService emailTokenService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String VALID_TOKEN = "123456";

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void saveSecureToken_성공() {
        doNothing().when(valueOperations).set(anyString(), anyString(), anyLong(), any());

        try (MockedStatic<SecureRandomUtil> mockedStatic = mockStatic(SecureRandomUtil.class)) {
            mockedStatic.when(SecureRandomUtil::generateSecureToken).thenReturn(VALID_TOKEN);

            String token = emailTokenService.saveSecureToken(TEST_EMAIL);

            assertNotNull(token);
            assertEquals(VALID_TOKEN, token);
            verify(valueOperations, times(1))
                    .set(eq("password_verification:" + TEST_EMAIL), eq(VALID_TOKEN), anyLong(), any());
        }
    }

    @Test
    void saveSecureToken_Redis_저장_실패() {
        doThrow(new RuntimeException("Redis 저장 실패"))
                .when(valueOperations).set(anyString(), anyString(), anyLong(), any());

        assertThrows(RuntimeException.class, () -> emailTokenService.saveSecureToken(TEST_EMAIL));
    }
}
