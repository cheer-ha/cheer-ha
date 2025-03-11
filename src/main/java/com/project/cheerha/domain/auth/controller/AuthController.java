package com.project.cheerha.domain.auth.controller;

import com.project.cheerha.common.cookie.CookieService;
import com.project.cheerha.common.dto.ApiResponseDto;
import com.project.cheerha.domain.auth.dto.request.CreateLoginRequestDto;
import com.project.cheerha.domain.auth.dto.request.CreateSignupRequestDto;
import com.project.cheerha.domain.auth.dto.request.VerifySignupRequestDto;
import com.project.cheerha.domain.auth.dto.response.*;
import com.project.cheerha.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<CreateSignupResponseDto>> signup(
            @Valid @RequestBody CreateSignupRequestDto requestDto
    ) {
        return ApiResponseDto.created(authService.signup(requestDto));
    }

    @PostMapping("/signup-verify")
    public ResponseEntity<ApiResponseDto<VerifySignupResponseDto>> verifySignup(
            @Valid @RequestBody VerifySignupRequestDto requestDto
    ){
        return ApiResponseDto.created(authService.verifySignup(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<CreateLoginResponseDto>> login(
            @Valid @RequestBody CreateLoginRequestDto requestDto,
            HttpServletResponse response
    ) {
        CreateLoginResponseDto responseDto = authService.login(requestDto);
        cookieService.createRefreshTokenCookie(responseDto.refreshToken(), response);
        CreateLoginResponseDto newResponseDto = new CreateLoginResponseDto("로그인 성공", responseDto.token(), null);
        return ApiResponseDto.success(newResponseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<CreateLogoutResponseDto>> logout(
            @RequestHeader("Authorization") String authHeader,
            HttpServletResponse response
    ) {
        CreateLogoutResponseDto responseDto = authService.logout(authHeader);
        cookieService.removeRefreshTokenCookie(response);
        return ApiResponseDto.success(responseDto);
    }

    /**
     * 사용자의 RefreshToken 을 이용해 AccessToken 을 재발급 해주는 컨트롤러 메서드
     * @return 현재 사용자의 새로운 AccessToken
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDto<RefreshAccessTokenResponseDto>> refreshAccessToken(
        HttpServletRequest request
    ) {
        String refreshToken = cookieService.getRefreshTokenCookie(request);
        return ApiResponseDto.success(authService.refreshAccessToken(refreshToken));
    }
}
