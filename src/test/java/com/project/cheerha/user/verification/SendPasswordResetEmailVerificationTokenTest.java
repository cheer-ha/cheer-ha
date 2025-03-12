package com.project.cheerha.user.verification;

import com.project.cheerha.common.email.sender.VerificationEmailSender;
import com.project.cheerha.common.exception.data.NotFoundException;
import com.project.cheerha.domain.user.service.EmailTokenService;
import com.project.cheerha.domain.user.dto.response.SendEmailVerificationResponseDto;
import com.project.cheerha.domain.user.repository.UserRepository;
import com.project.cheerha.domain.user.service.UserEmailVerificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SendPasswordResetEmailVerificationTokenTest {

    @InjectMocks
    UserEmailVerificationService userEmailVerificationService;

    @Mock
    VerificationEmailSender verificationEmailSender;

    @Mock
    EmailTokenService emailTokenService;

    @Mock
    UserRepository userRepository;

    @Test
    void sendPasswordResetEmailVerificationToken_성공() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        when(emailTokenService.saveToken(anyString(), anyString())).thenReturn("testToken");
        doNothing().when(verificationEmailSender).sendVerificationEmail(anyString(), anyString());

        SendEmailVerificationResponseDto response = userEmailVerificationService.sendPasswordResetEmailVerificationToken("test@example.com");
        assertNotNull(response);
    }

    @Test
    void sendPasswordResetEmailVerificationToken_이메일없음_예외발생() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userEmailVerificationService.sendPasswordResetEmailVerificationToken("test@example.com"));
    }
}
