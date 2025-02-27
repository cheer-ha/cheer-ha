package com.project.cheerha.domain.user.controller;

import com.project.cheerha.common.annotation.Auth;
import com.project.cheerha.common.dto.ApiResponseDto;
import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.user.dto.request.VerifyNotificationCodeRequestDto;
import com.project.cheerha.domain.user.dto.request.VerifyPasswordResetCodeRequestDto;
import com.project.cheerha.domain.user.dto.request.SendPasswordResetEmailVerificationCodeRequestDto;
import com.project.cheerha.domain.user.dto.response.ActivateNotificationResponseDto;
import com.project.cheerha.domain.user.dto.response.VerifyPasswordResetCodeResponseDto;
import com.project.cheerha.domain.user.dto.response.SendEmailVerificationResponseDto;
import com.project.cheerha.domain.user.service.UserEmailVerificationService;
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

    private final UserEmailVerificationService userEmailVerificationService;

    @PostMapping("/send-notification-verify")
    public ResponseEntity<ApiResponseDto<SendEmailVerificationResponseDto>> sendNotificationVerifyEmailVerificationCode(
            @Auth AuthUser authUser
    ) {
        SendEmailVerificationResponseDto responseDto = userEmailVerificationService.sendNotificationVerifyEmailVerificationCode(authUser.id());
        return ApiResponseDto.success(responseDto);
    }

    @PostMapping("/notification-verify")
    public ResponseEntity<ApiResponseDto<ActivateNotificationResponseDto>> verifyNotificationCode(
            @RequestBody VerifyNotificationCodeRequestDto requestDto,
            @Auth AuthUser authUser
    ) {
        userEmailVerificationService.verifyNotificationEmailCode(authUser.id(), requestDto.code());
        ActivateNotificationResponseDto responseDto = userEmailVerificationService.activateNotification(authUser.id());
        return ApiResponseDto.success(responseDto);
    }

    @PostMapping("/send-password-reset-verify")
    public ResponseEntity<ApiResponseDto<SendEmailVerificationResponseDto>> sendPasswordResetEmailVerificationCode(
            @RequestBody SendPasswordResetEmailVerificationCodeRequestDto requestDto
    ) {
        SendEmailVerificationResponseDto responseDto = userEmailVerificationService.sendPasswordResetEmailVerificationCode(requestDto.email());
        return ApiResponseDto.success(responseDto);
    }

    @PostMapping("/password-reset-verify")
    public ResponseEntity<ApiResponseDto<VerifyPasswordResetCodeResponseDto>> verifyPasswordResetCode(
            @RequestBody VerifyPasswordResetCodeRequestDto requestDto
    ) {
        userEmailVerificationService.verifyPasswordResetEmailCode(requestDto.email(), requestDto.code());
        VerifyPasswordResetCodeResponseDto responseDto = userEmailVerificationService.createPasswordResetToken(requestDto.email());
        return ApiResponseDto.success(responseDto);
    }

}
