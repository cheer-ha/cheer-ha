package com.project.cheerha.domain.userkeyword.dto.response;

import com.project.cheerha.domain.keyword.dto.response.KeywordDto;

import java.util.List;

public record ReadUserKeywordResponseDto(Long userKeywordId, List<KeywordDto> keywordDtoList) {

    public static ReadUserKeywordResponseDto toDto(Long userKeywordId, List<KeywordDto> keywordDtoList) {
        return new ReadUserKeywordResponseDto(userKeywordId, keywordDtoList);
    }
}