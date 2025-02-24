package com.project.cheerha.domain.user.controller;

import com.project.cheerha.common.annotation.Auth;
import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.user.dto.response.ReadUserResponseDto;
import com.project.cheerha.domain.user.service.UserService;
import com.project.cheerha.common.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

    @PatchMapping("/password/email-verification")
    public ResponseEntity<ApiResponseDto<UpdatePasswordWithEmailResponseDto>> updatePasswordWithEmailVerification(
            @RequestBody UpdatePasswordWithEmailRequestDto requestDto
    ) {
        UpdatePasswordWithEmailResponseDto responseDto = userService.updatePasswordWithEmailVerification(requestDto);
        return ApiResponseDto.success(responseDto);
    }

    @PostMapping("/email-verification")
    public ResponseEntity<ApiResponseDto<SendEmailVerificationResponseDto>> sendEmailVerificationCode(
            @RequestBody SendEmailVerificationRequestDto requestDto
    ) {
        SendEmailVerificationResponseDto responseDto = emailService.sendVerificationCode(requestDto);
        return ApiResponseDto.success(responseDto);
    }

    @PostMapping("/email-verification/verify")
    public ResponseEntity<ApiResponseDto<VerifyEmailCodeResponseDto>> verifyEmailCode(
            @RequestBody VerifyEmailCodeRequestDto requestDto
    ) {
        VerifyEmailCodeResponseDto responseDto = emailService.verifyEmailCode(requestDto);
        return ApiResponseDto.success(responseDto);
    }


}
