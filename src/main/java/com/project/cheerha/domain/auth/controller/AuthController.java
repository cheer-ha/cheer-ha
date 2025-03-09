package com.project.cheerha.domain.auth.controller;

import com.project.cheerha.common.dto.ApiResponseDto;
import com.project.cheerha.domain.auth.dto.request.CreateLoginRequestDto;
import com.project.cheerha.domain.auth.dto.request.CreateSignupRequestDto;
import com.project.cheerha.domain.auth.dto.request.VerifySignupRequestDto;
import com.project.cheerha.domain.auth.dto.response.*;
import com.project.cheerha.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<CreateSignupResponseDto>> signup(
            @Valid @RequestBody CreateSignupRequestDto dto) {
        return ApiResponseDto.created(authService.signup(dto));
    }

    @PostMapping("/signup-verify")
    public ResponseEntity<ApiResponseDto<VerifySignupResponseDto>> verifySignup(
            @Valid @RequestBody VerifySignupRequestDto dto){
        return ApiResponseDto.created(authService.verifySignup(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<CreateLoginResponseDto>> login(
            @Valid @RequestBody CreateLoginRequestDto dto) {
        return ApiResponseDto.success(authService.login(dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<CreateLogoutResponseDto>> logout(
            @RequestHeader("Authorization") String authHeader) {
        return ApiResponseDto.success(authService.logout(authHeader));
    }

    /**
     * 사용자의 RefreshToken 을 이용해 AccessToken 을 재발급 해주는 컨트롤러 메서드
     * @param refreshToken 현재 사용자의 RefreshToken
     * @return 현재 사용자의 새로운 AccessToken
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDto<RefreshAccessTokenResponseDto>> refreshAccessToken(
        @RequestHeader("Authorization") String refreshToken
    ) {
        return ApiResponseDto.success(authService.refreshAccessToken(refreshToken));
    }
}
