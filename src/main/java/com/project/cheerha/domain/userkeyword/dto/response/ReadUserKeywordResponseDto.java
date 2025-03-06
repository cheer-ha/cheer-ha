package com.project.cheerha.domain.userkeyword.dto.response;

import com.project.cheerha.domain.keyword.dto.response.KeywordDto;

import java.util.List;

public record ReadUserKeywordResponseDto(List<KeywordDto> keywordDtoList) {

    public static ReadUserKeywordResponseDto toDto(List<KeywordDto> keywordDtoList) {
        return new ReadUserKeywordResponseDto(keywordDtoList);
    }
}