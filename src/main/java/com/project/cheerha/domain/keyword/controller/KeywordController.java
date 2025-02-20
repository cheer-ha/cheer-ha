package com.project.cheerha.domain.keyword.controller;

import com.project.cheerha.common.dto.ApiResponseDto;
import com.project.cheerha.domain.keyword.dto.request.ReadKeywordAgeRequestDto;
import com.project.cheerha.domain.keyword.dto.response.KeywordCustomAgeResponseDto;
import com.project.cheerha.domain.keyword.dto.response.ReadKeywordResponseDto;
import com.project.cheerha.domain.keyword.service.KeywordService;
import jakarta.validation.Valid;
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

    /**
     * 최소, 최대 연령대를 사용자가 커스터마이징하여 추가된 키워드를 상위 10개를 조회할 수 있는 로직입니다.
     * Valid 어노테이션으로 해당 연령대가 아닌 값이 나오면 예외가 발생하도록 설정했습니다.
     * @param requestDto 사용자가 body에 json 형태로 minAge, maxAge 입력
     * @return 커스텀 된 키워드 상위 10개를 List로 반환
     */
    @GetMapping("/popular")
    public ResponseEntity<ApiResponseDto<List<KeywordCustomAgeResponseDto>>> readTop10KeywordsByAgeGroup(
        @Valid @RequestBody ReadKeywordAgeRequestDto requestDto
    ) {
        List<KeywordCustomAgeResponseDto> dtoList = keywordService.readTop10KeywordsByAgeGroup(requestDto);
        return ApiResponseDto.success(dtoList);
    }
}
