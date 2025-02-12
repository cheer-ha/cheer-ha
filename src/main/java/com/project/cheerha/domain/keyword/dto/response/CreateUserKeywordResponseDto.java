package com.project.cheerha.domain.keyword.dto.response;

import java.util.List;

public record CreateUserKeywordResponseDto(List<String> keywordList) {

    public static CreateUserKeywordResponseDto of(List<String> keywordList) {
        return new CreateUserKeywordResponseDto(keywordList);
    }
}
