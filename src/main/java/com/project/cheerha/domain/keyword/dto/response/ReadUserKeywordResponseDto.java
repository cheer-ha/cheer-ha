package com.project.cheerha.domain.keyword.dto.response;

import java.util.List;

public record ReadUserKeywordResponseDto(List<KeywordDto> keywordListChosenByUser) {
    // 응답 화면에서 'keywordListChosenByUser'로 나오도록 위와 같이 변수 명명

    public static ReadUserKeywordResponseDto toDto(List<KeywordDto> keywordDtoList) {
        return new ReadUserKeywordResponseDto(keywordDtoList);
    }
}