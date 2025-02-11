package com.project.cheerha.domain.auth.controller;

import com.project.cheerha.domain.auth.dto.request.CreateLoginRequestDto;
import com.project.cheerha.domain.auth.dto.request.CreateSignupRequestDto;
import com.project.cheerha.domain.auth.dto.response.CreateLoginResponseDto;
import com.project.cheerha.domain.auth.dto.response.CreateLogoutResponseDto;
import com.project.cheerha.domain.auth.dto.response.CreateSignupResponseDto;
import com.project.cheerha.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<CreateSignupResponseDto> signup(
        @Valid @RequestBody CreateSignupRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<CreateLoginResponseDto> login(
        @Valid @RequestBody CreateLoginRequestDto dto
    ){
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<CreateLogoutResponseDto> logout(
        @RequestHeader("Authorization") String authHeader
    ){
        return ResponseEntity.status(HttpStatus.OK).body(authService.logout(authHeader));
    }

}
