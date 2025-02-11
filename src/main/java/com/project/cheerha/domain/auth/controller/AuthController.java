package com.project.cheerha.domain.auth.controller;

import com.project.cheerha.domain.auth.dto.request.CreateUserRequestDto;
import com.project.cheerha.domain.auth.dto.response.CreateUserResponseDto;
import com.project.cheerha.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public CreateUserResponseDto signup(
        @Valid @RequestBody CreateUserRequestDto dto
    ) {
        return authService.signup(dto);
    }
}
