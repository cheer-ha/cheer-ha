package com.project.cheerha.domain.keyword.dto.response;

import java.util.List;

public record ReadUserKeywordResponseDto(List<String> keywordNameListChosenByUser) {

    public static ReadUserKeywordResponseDto toDto(List<String> keywordNameListChosenByUser) {
        return new ReadUserKeywordResponseDto(keywordNameListChosenByUser);
    }
}