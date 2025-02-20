package com.project.cheerha.domain.keyword.controller;

import com.project.cheerha.common.dto.ApiResponseDto;
import com.project.cheerha.domain.keyword.dto.request.ReadKeywordAgeRequestDto;
import com.project.cheerha.domain.keyword.dto.response.KeywordCustomAgeResponseDto;
import com.project.cheerha.domain.keyword.dto.response.ReadKeywordResponseDto;
import com.project.cheerha.domain.keyword.service.KeywordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/keywords")
@RestController
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<ReadKeywordResponseDto>> readAllKeywords(
            @RequestParam(value = "searchTerm", required = false) String searchTerm
    ) {
        ReadKeywordResponseDto responseDto = keywordService.readKeywords(searchTerm);
        return ApiResponseDto.success(responseDto);
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponseDto<List<KeywordCustomAgeResponseDto>>> readTop10KeywordsByAgeGroup(
        @RequestBody ReadKeywordAgeRequestDto requestDto
    ) {
        List<KeywordCustomAgeResponseDto> dtoList = keywordService.readTop10KeywordsByAgeGroup(requestDto);
        return ApiResponseDto.success(dtoList);
    }
}
