package com.project.cheerha.domain.user.controller;

import com.project.cheerha.common.annotation.Auth;
import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.user.dto.response.ReadUserResponseDto;
import com.project.cheerha.domain.user.service.UserService;
import com.project.cheerha.common.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
