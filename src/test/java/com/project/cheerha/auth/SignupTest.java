package com.project.cheerha.auth;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.common.util.PasswordEncoder;
import com.project.cheerha.domain.auth.dto.request.CreateSignupRequestDto;
import com.project.cheerha.domain.auth.dto.response.CreateSignupResponseDto;
import com.project.cheerha.domain.auth.service.AuthService;
import com.project.cheerha.domain.user.entity.User;
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
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void testSignup_가입에_성공했을때() {
        //given
        CreateSignupRequestDto dto = new CreateSignupRequestDto(
                "test@example.com",
                "password",
                "tester",
                20,
                0
                );
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");

        //when
        CreateSignupResponseDto response = authService.signup(dto);

        //then
        assertNotNull(response);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSignup_이미_존재하는_이메일일때() {
        //given
        CreateSignupRequestDto dto = new CreateSignupRequestDto(
                "test@example.com",
                "password123",
                "tester",
                20,
                0
        );
        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        //when & then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.signup(dto));
        assertEquals(ClientErrorCode.ALREADY_EXIST_EMAIL.getMessage(), exception.getMessage());
    }
}
