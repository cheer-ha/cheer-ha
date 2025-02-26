package com.project.cheerha.domain.user.controller;

import com.project.cheerha.common.annotation.Auth;
import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.user.dto.request.*;
import com.project.cheerha.domain.user.dto.response.*;
import com.project.cheerha.domain.user.service.EmailVerificationService;
import com.project.cheerha.domain.user.service.UserPasswordService;
import com.project.cheerha.domain.user.service.UserService;
import com.project.cheerha.common.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserPasswordService userPasswordService;
    private final EmailVerificationService emailVerificationService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<ReadUserResponseDto>> readUser(
            @Auth AuthUser authUser
    ) {
        ReadUserResponseDto responseDto = userService.readUser(authUser);
        return ApiResponseDto.success(responseDto);
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponseDto<UpdatePasswordResponseDto>> updatePassword(
            @Auth AuthUser authUser,
            @RequestBody UpdatePasswordRequestDto requestDto
    ) {
        UpdatePasswordResponseDto responseDto = userPasswordService.updatePassword(authUser.id(), requestDto);
        return ApiResponseDto.success(responseDto);
    }

    @PatchMapping("/password-reset")
    public ResponseEntity<ApiResponseDto<UpdatePasswordResponseDto>> resetPassword(
            @RequestBody ResetPasswordRequestDto requestDto
    ) {
        UpdatePasswordResponseDto responseDto = userPasswordService.resetPassword(requestDto);
        return ApiResponseDto.success(responseDto);
    }

    @PostMapping("/email-verification/password-reset-verify")
    public ResponseEntity<ApiResponseDto<SendEmailVerificationResponseDto>> sendPasswordResetEmailVerificationCode(
            @RequestBody SendPasswordResetEmailVerificationCodeRequestDto requestDto
    ) {
        SendEmailVerificationResponseDto responseDto = emailVerificationService.sendPasswordResetEmailVerificationCode(requestDto.email());
        return ApiResponseDto.success(responseDto);
    }

    @PostMapping("/email-verification/notification-verify")
    public ResponseEntity<ApiResponseDto<SendEmailVerificationResponseDto>> sendNotificationVerifyEmailVerificationCode(
            @Auth AuthUser authUser
    ) {
        SendEmailVerificationResponseDto responseDto = emailVerificationService.sendNotificationVerifyEmailVerificationCode(authUser.id());
        return ApiResponseDto.success(responseDto);
    }

    @PostMapping("/email-verification/verify-password-reset")
    public ResponseEntity<ApiResponseDto<Object>> verifyPasswordResetCode(
            @RequestBody PasswordResetVerifyEmailCodeRequestDto requestDto
    ) {
        emailVerificationService.verifyPasswordResetEmailCode(requestDto.email(), requestDto.code());
        CreatePasswordResetTokenResponseDto responseDto = emailVerificationService.createPasswordResetToken(requestDto.email());
        return ApiResponseDto.success(responseDto);
    }

    @PostMapping("/email-verification/verify-notification")
    public ResponseEntity<ApiResponseDto<Object>> verifyNotificationCode(
            @RequestBody NotificationVerifyEmailCodeRequestDto requestDto,
            @Auth AuthUser authUser
    ) {
        emailVerificationService.verifyNotificationEmailCode(authUser.id(), requestDto.code());
        ActivateNotificationResponseDto responseDto = emailVerificationService.activateNotifications(authUser.id());
        return ApiResponseDto.success(responseDto);
    }

}
