package com.project.cheerha.user.verification;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.common.exception.data.NotFoundException;
import com.project.cheerha.common.redis.email.EmailTokenService;
import com.project.cheerha.domain.user.repository.UserRepository;
import com.project.cheerha.domain.user.service.UserEmailVerificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class VerifyPasswordResetEmailTokenTest {

    @InjectMocks
    UserEmailVerificationService userEmailVerificationService;

    @Mock
    UserRepository userRepository;

    @Mock
    EmailTokenService emailTokenService;

    @Test
    void verifyPasswordResetEmailToken_성공() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        doNothing().when(emailTokenService).verifyEmailToken(anyString(), anyString(), anyString());

        assertDoesNotThrow(() -> userEmailVerificationService.verifyPasswordResetEmailToken("test@example.com", "validToken"));
    }

    @Test
    void verifyPasswordResetEmailToken_이메일없음_예외발생() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userEmailVerificationService.verifyPasswordResetEmailToken("test@example.com", "validToken"));
    }

    @Test
    void verifyPasswordResetEmailToken_잘못된토큰_예외발생() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        doThrow(new BadRequestException(ClientErrorCode.INVALID_EMAIL_VERIFICATION_TOKEN))
                .when(emailTokenService).verifyEmailToken(anyString(), anyString(), anyString());

        assertThrows(BadRequestException.class, () -> userEmailVerificationService.verifyPasswordResetEmailToken("test@example.com", "invalidToken"));
    }
}
