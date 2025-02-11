package com.project.cheerha.domain.keyword.controller;

import com.project.cheerha.domain.keyword.dto.request.CreateUserKeywordRequestDto;
import com.project.cheerha.domain.keyword.dto.response.CreateUserKeywordResponseDto;
import com.project.cheerha.domain.keyword.service.UserKeywordService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping
@RestController
@RequiredArgsConstructor
public class UserKeywordController {

    private final UserKeywordService userKeywordService;

    @PostMapping("/user/keywords")
    public ResponseEntity<List<CreateUserKeywordResponseDto>> registerUserKeywordList(
        @RequestBody List<CreateUserKeywordRequestDto> requestDtoList,
        HttpServletRequest request
    ) {
        Long userId = getUserIdFromToken(request);

        log.info("userId from Token: {}", userId);

        List<CreateUserKeywordResponseDto> responseDtoList = new ArrayList<>();

        responseDtoList = userKeywordService.createUserKeyword(
            userId,
            requestDtoList
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDtoList);
    }

    private Long getUserIdFromToken(HttpServletRequest request) {
        return (Long) request.getSession().getAttribute("userId");
    }
}