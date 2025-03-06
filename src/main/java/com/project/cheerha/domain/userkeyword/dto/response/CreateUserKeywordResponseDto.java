package com.project.cheerha.domain.userkeyword.dto.response;

import java.util.List;

public record CreateUserKeywordResponseDto(List<String> keywordList) {

    public static CreateUserKeywordResponseDto toDto(List<String> keywordList) {
        return new CreateUserKeywordResponseDto(keywordList);
    }
}
