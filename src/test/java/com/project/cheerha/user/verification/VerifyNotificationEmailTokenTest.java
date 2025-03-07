package com.project.cheerha.user.verification;

import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.common.exception.data.NotFoundException;
import com.project.cheerha.common.redis.email.EmailTokenService;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class VerifyNotificationEmailTokenTest {

    @InjectMocks
    UserEmailVerificationService userEmailVerificationService;

    @Mock
    UserFindByService userFindByService;

    @Mock
    EmailTokenService emailTokenService;

    @Test
    void verifyNotificationEmailToken_성공() {
        User mockUser = TestUtils.createEntity(User.class, Map.of(
                "email", "test@example.com",
                "isNotificationEnabled", false
        ));
        when(userFindByService.findById(1L)).thenReturn(mockUser);
        doNothing().when(emailTokenService).verifyEmailToken(anyString(), anyString(), anyString());

        assertDoesNotThrow(() -> userEmailVerificationService.verifyNotificationEmailToken(1L, "validToken"));
    }

    @Test
    void verifyNotificationEmailToken_유저없음_예외발생() {
        when(userFindByService.findById(1L)).thenThrow(new NotFoundException(DataErrorCode.USER_NOT_FOUND));

        assertThrows(NotFoundException.class, () -> userEmailVerificationService.verifyNotificationEmailToken(1L, "validToken"));
    }
}
