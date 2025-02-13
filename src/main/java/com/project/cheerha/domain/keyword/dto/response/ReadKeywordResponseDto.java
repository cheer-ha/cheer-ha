package com.project.cheerha.domain.keyword.dto.response;

import java.util.List;

public record ReadKeywordResponseDto(List<KeywordDto> KeywordDtoList) {

    public static ReadKeywordResponseDto toDto(List<KeywordDto> keywordDtoList) {
        return new ReadKeywordResponseDto(keywordDtoList);
    }
}