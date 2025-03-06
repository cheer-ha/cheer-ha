package com.project.cheerha.auth;

import com.project.cheerha.common.email.sender.VerificationEmailSender;
import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.common.redis.email.EmailTokenService;
import com.project.cheerha.domain.auth.dto.request.CreateSignupRequestDto;
import com.project.cheerha.domain.auth.dto.response.CreateSignupResponseDto;
import com.project.cheerha.domain.auth.service.AuthService;
import com.project.cheerha.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignupTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailTokenService emailTokenService;

    @Mock
    private VerificationEmailSender verificationEmailSender;

    @InjectMocks
    private AuthService authService;

    @Test
    void testSignup_이메일전송에_성공했을때() {
        //given
        CreateSignupRequestDto dto = new CreateSignupRequestDto(
                "test@example.com"
                );
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);

        //when
        CreateSignupResponseDto response = authService.signup(dto);

        //then
        assertNotNull(response);
    }

    @Test
    void testSignup_이미_존재하는_이메일일때() {
        //given
        CreateSignupRequestDto dto = new CreateSignupRequestDto(
                "test@example.com"
        );
        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        //when & then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.signup(dto));
        assertEquals(ClientErrorCode.ALREADY_EXIST_EMAIL.getMessage(), exception.getMessage());
    }
}
