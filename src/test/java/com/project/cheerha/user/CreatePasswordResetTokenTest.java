package com.project.cheerha.user;

import com.project.cheerha.common.exception.data.NotFoundException;
import com.project.cheerha.common.redis.email.EmailTokenService;
import com.project.cheerha.domain.user.dto.response.VerifyPasswordResetTokenResponseDto;
import com.project.cheerha.domain.user.repository.UserRepository;
import com.project.cheerha.domain.user.service.UserEmailVerificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CreatePasswordResetTokenTest {

    @InjectMocks
    UserEmailVerificationService userEmailVerificationService;

    @Mock
    UserRepository userRepository;

    @Mock
    EmailTokenService emailTokenService;

    @Test
    void createPasswordResetToken_성공() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        when(emailTokenService.saveSecureToken(anyString())).thenReturn("secureToken");

        VerifyPasswordResetTokenResponseDto response = userEmailVerificationService.createPasswordResetToken("test@example.com");
        assertNotNull(response);
        assertEquals("secureToken", response.resetToken());
    }

    @Test
    void createPasswordResetToken_이메일없음_예외발생() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userEmailVerificationService.createPasswordResetToken("test@example.com"));
    }
}
