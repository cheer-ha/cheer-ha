package com.project.cheerha.user;

import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.common.exception.data.NotFoundException;
import com.project.cheerha.domain.user.dto.response.ActivateNotificationResponseDto;
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
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ActivateNotificationTest {

    @InjectMocks
    UserEmailVerificationService userEmailVerificationService;

    @Mock
    UserFindByService userFindByService;

    @Test
    void activateNotification_성공() {
        User mockUser = TestUtils.spy(User.class, Map.of(
                "email", "test@example.com",
                "isNotificationEnabled", false
        ));
        when(userFindByService.findById(1L)).thenReturn(mockUser);

        ActivateNotificationResponseDto response = userEmailVerificationService.activateNotification(1L);
        assertNotNull(response);
    }

    @Test
    void activateNotification_유저없음_예외발생() {
        when(userFindByService.findById(1L)).thenThrow(new NotFoundException(DataErrorCode.USER_NOT_FOUND));

        assertThrows(NotFoundException.class, () -> userEmailVerificationService.activateNotification(1L));
    }
}
