package com.project.cheerha.domain.user.controller;

import com.project.cheerha.common.annotation.Auth;
import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.user.dto.response.ReadUserResponseDto;
import com.project.cheerha.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ReadUserResponseDto> readUser(
        @Auth AuthUser authUser
    ) {
       // return ResponseEntity.status(HttpStatus.OK).body(userService.readUser(authUser));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
