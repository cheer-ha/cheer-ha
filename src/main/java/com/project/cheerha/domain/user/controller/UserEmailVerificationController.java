package com.project.cheerha.domain.user.controller;

import com.project.cheerha.common.annotation.Auth;
import com.project.cheerha.common.dto.ApiResponseDto;
import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.user.dto.request.NotificationVerifyEmailCodeRequestDto;
import com.project.cheerha.domain.user.dto.request.PasswordResetVerifyEmailCodeRequestDto;
import com.project.cheerha.domain.user.dto.request.SendPasswordResetEmailVerificationCodeRequestDto;
import com.project.cheerha.domain.user.dto.response.ActivateNotificationResponseDto;
import com.project.cheerha.domain.user.dto.response.CreatePasswordResetTokenResponseDto;
import com.project.cheerha.domain.user.dto.response.SendEmailVerificationResponseDto;
import com.project.cheerha.domain.user.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users/email-verification")
@RestController
@RequiredArgsConstructor
public class UserEmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/notification-verify")
    public ResponseEntity<ApiResponseDto<SendEmailVerificationResponseDto>> sendNotificationVerifyEmailVerificationCode(
            @Auth AuthUser authUser
    ) {
        SendEmailVerificationResponseDto responseDto = emailVerificationService.sendNotificationVerifyEmailVerificationCode(authUser.id());
        return ApiResponseDto.success(responseDto);
    }

    @PostMapping("/verify-notification")
    public ResponseEntity<ApiResponseDto<Object>> verifyNotificationCode(
            @RequestBody NotificationVerifyEmailCodeRequestDto requestDto,
            @Auth AuthUser authUser
    ) {
        emailVerificationService.verifyNotificationEmailCode(authUser.id(), requestDto.code());
        ActivateNotificationResponseDto responseDto = emailVerificationService.activateNotifications(authUser.id());
        return ApiResponseDto.success(responseDto);
    }

    @PostMapping("/password-reset-verify")
    public ResponseEntity<ApiResponseDto<SendEmailVerificationResponseDto>> sendPasswordResetEmailVerificationCode(
            @RequestBody SendPasswordResetEmailVerificationCodeRequestDto requestDto
    ) {
        SendEmailVerificationResponseDto responseDto = emailVerificationService.sendPasswordResetEmailVerificationCode(requestDto.email());
        return ApiResponseDto.success(responseDto);
    }

    @PostMapping("/verify-password-reset")
    public ResponseEntity<ApiResponseDto<Object>> verifyPasswordResetCode(
            @RequestBody PasswordResetVerifyEmailCodeRequestDto requestDto
    ) {
        emailVerificationService.verifyPasswordResetEmailCode(requestDto.email(), requestDto.code());
        CreatePasswordResetTokenResponseDto responseDto = emailVerificationService.createPasswordResetToken(requestDto.email());
        return ApiResponseDto.success(responseDto);
    }

}
