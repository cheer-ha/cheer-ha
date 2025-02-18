package com.project.cheerha.domain.history.controller;

import com.project.cheerha.common.annotation.Auth;
import com.project.cheerha.common.dto.ApiResponseDto;
import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/search/history")
@RestController
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<String>>> readAllHistories(
            @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();
        List<String> SearchTermsList = historyService.getRecentSearchTerms(userId);

        if (SearchTermsList.isEmpty()) {
            SearchTermsList = historyService.getRecentSearchTerms(userId); // 다시 조회
        }

        return ApiResponseDto.success(SearchTermsList);
    }

}
