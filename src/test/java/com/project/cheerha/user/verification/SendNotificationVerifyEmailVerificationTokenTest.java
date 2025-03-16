package com.project.cheerha.user.verification;

import com.project.cheerha.common.email.sender.VerificationEmailSender;
import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.domain.user.service.EmailTokenService;
import com.project.cheerha.domain.user.dto.response.SendEmailVerificationResponseDto;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserEmailVerificationService;
import com.project.cheerha.domain.user.service.UserFindByService;
import com.project.cheerha.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SendNotificationVerifyEmailVerificationTokenTest {

    @InjectMocks
    UserEmailVerificationService userEmailVerificationService;

    @Mock
    EmailTokenService emailTokenService;

    @Mock
    VerificationEmailSender verificationEmailSender;

    @Mock
    UserFindByService userFindByService;

    @Test
    void TestSendNotificationVerifyEmail_토큰_이메일_전송_성공() {
        Long userId = 1L;
        User mockUser = TestUtils.createEntity(User.class, Map.of(
                "email", "test@example.com",
                "isNotificationEnabled", false
        ));
        when(userFindByService.findById(userId)).thenReturn(mockUser);
        when(emailTokenService.saveToken(anyString(), anyString())).thenReturn("testToken");

        SendEmailVerificationResponseDto response =
                userEmailVerificationService.sendNotificationVerifyEmailVerificationToken(userId);

        assertNotNull(response);
        verify(emailTokenService, times(1)).saveToken(anyString(), anyString());
        verify(verificationEmailSender, times(1)).sendVerificationEmail(eq(mockUser.getEmail()), eq("testToken"));
    }

    @Test
    void TestSendNotificationVerifyEmail_이미_알림_구독중인_유저() {
        Long userId = 2L;
        User mockUser = TestUtils.createEntity(User.class, Map.of(
                "email", "test@example.com",
                "isNotificationEnabled", true
        ));
        when(userFindByService.findById(userId)).thenReturn(mockUser);

        assertThrows(BadRequestException.class,
                () -> userEmailVerificationService.sendNotificationVerifyEmailVerificationToken(userId));
    }

}
