package com.project.cheerha.domain.keyword.controller;

import com.project.cheerha.common.dto.ApiResponseDto;
import com.project.cheerha.domain.keyword.dto.response.KeywordCustomAgeResponseDto;
import com.project.cheerha.domain.keyword.dto.response.ReadKeywordResponseDto;
import com.project.cheerha.domain.keyword.service.KeywordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * 최소, 최대 연령대를 사용자가 커스터마이징하여 추가된 키워드를 상위 10개를 조회할 수 있는 로직입니다.
     * @param minAge 최소연령
     * @param maxAge 최대연령
     * @return 커스텀 된 키워드 상위 10개를 List로 반환
     */
    @GetMapping("/popular")
    public ResponseEntity<ApiResponseDto<List<KeywordCustomAgeResponseDto>>> readTop10KeywordsByAgeGroup(
        @RequestParam(value = "minAge") int minAge,
        @RequestParam(value = "maxAge") int maxAge
    ) {
        List<KeywordCustomAgeResponseDto> dtoList = keywordService.readTop10KeywordsByAgeBetween(minAge, maxAge);
        return ApiResponseDto.success(dtoList);
    }
}
