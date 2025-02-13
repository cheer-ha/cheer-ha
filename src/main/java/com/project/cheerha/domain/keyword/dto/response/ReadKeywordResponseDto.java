package com.project.cheerha.domain.keyword.dto.response;

import java.util.List;

public record ReadKeywordResponseDto(List<KeywordDto> KeywordList) {
    // 응답 화면에서 'KeywordList'로 나오도록 'KeywordDtoList' 대신 'KeywordList'로 명명

    public static ReadKeywordResponseDto toDto(List<KeywordDto> keywordDtoList) {
        return new ReadKeywordResponseDto(keywordDtoList);
    }
}