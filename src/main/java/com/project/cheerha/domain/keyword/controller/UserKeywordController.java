package com.project.cheerha.domain.keyword.controller;

import com.project.cheerha.common.annotation.Auth;
import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.keyword.dto.request.CreateUserKeywordRequestDto;
import com.project.cheerha.domain.keyword.dto.request.DeleteUserKeywordRequestDto;
import com.project.cheerha.domain.keyword.dto.response.CreateUserKeywordResponseDto;
import com.project.cheerha.domain.keyword.service.UserKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users/keywords")
@RestController
@RequiredArgsConstructor
public class UserKeywordController {

    private final UserKeywordService userKeywordService;

    @PostMapping
    public ResponseEntity<CreateUserKeywordResponseDto> createUserKeywordList(
        @RequestBody CreateUserKeywordRequestDto requestDto,
        @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();

        CreateUserKeywordResponseDto responseDto = userKeywordService.createUserKeyword(
            userId,
            requestDto
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserKeyword(
        @RequestBody DeleteUserKeywordRequestDto requestDto,
        @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();

        userKeywordService.deleteUserKeyword(userId, requestDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}