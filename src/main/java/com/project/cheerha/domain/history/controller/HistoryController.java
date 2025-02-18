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

    /**
     * 사용자의 최근 검색어 목록을 조회하는 API입니다.
     *
     * Redis에 저장된 최근 검색어 목록을 반환합니다.
     * 첫번째 조회 시 검색어가 없다면, 다시 조회하여 DB에서 검색어 목록을 가져옵니다.
     *
     * @param authUser 로그인한 유저의 정보
     * @return 최근 검색어 목록 (10개)
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<String>>> readAllHistories(
            @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();
        List<String> SearchTermsList = historyService.getRecentSearchTerms(userId);

        if (SearchTermsList.isEmpty()) {
            SearchTermsList = historyService.getRecentSearchTerms(userId);
        }

        return ApiResponseDto.success(SearchTermsList);
    }

}
