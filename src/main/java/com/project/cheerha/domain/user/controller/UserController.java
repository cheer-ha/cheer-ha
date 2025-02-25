package com.project.cheerha.domain.user.controller;

import com.project.cheerha.common.annotation.Auth;
import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.user.dto.request.SendEmailVerificationRequestDto;
import com.project.cheerha.domain.user.dto.request.UpdatePasswordRequestDto;
import com.project.cheerha.domain.user.dto.request.UpdatePasswordWithEmailRequestDto;
import com.project.cheerha.domain.user.dto.request.VerifyEmailCodeRequestDto;
import com.project.cheerha.domain.user.dto.response.*;
import com.project.cheerha.domain.user.service.EmailVerificationService;
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
        UpdatePasswordResponseDto responseDto = userService.updatePassword(authUser, requestDto);
        return ApiResponseDto.success(responseDto);
    }

    @PatchMapping("/password/email-verification")   //token 필요
    public ResponseEntity<ApiResponseDto<UpdatePasswordResponseDto>> updatePasswordWithEmailVerification(
            @RequestBody UpdatePasswordWithEmailRequestDto requestDto
    ) {
        UpdatePasswordResponseDto responseDto = userService.updatePasswordWithEmailVerification(requestDto);
        return ApiResponseDto.success(responseDto);
    }

    @PostMapping("/email-verification")
    public ResponseEntity<ApiResponseDto<SendEmailVerificationResponseDto>> sendEmailVerificationCode(
            @RequestBody SendEmailVerificationRequestDto requestDto
    ) {
        SendEmailVerificationResponseDto responseDto = emailVerificationService.sendVerificationCode(requestDto);
        return ApiResponseDto.success(responseDto);
    }

    @PostMapping("/email-verification/verify")
    public ResponseEntity<ApiResponseDto<Object>> verifyEmailCode(
            @RequestBody VerifyEmailCodeRequestDto requestDto
    ) {
        boolean isVerified = emailVerificationService.verifyEmailCode(requestDto.email(), requestDto.code());

        if (isVerified) {
            switch (requestDto.purpose()) {
                case "PASSWORD_RESET":
                    CreatePasswordResetTokenResponseDto responseDto = emailVerificationService.createPasswordResetToken(requestDto.email());
                    return ApiResponseDto.success(responseDto);
                case "NOTIFICATION":
                    ActivateNotificationResponseDto responseDTo =  emailVerificationService.activateNotifications(requestDto.email());
                    return ApiResponseDto.success(responseDTo);
                default:
                    throw new IllegalArgumentException("알 수 없는 요청");    //고칠예정
            }
        } else {
            throw new IllegalArgumentException("인증 코드가 잘못되었습니다");   //고칠예정
        }
    }
}
