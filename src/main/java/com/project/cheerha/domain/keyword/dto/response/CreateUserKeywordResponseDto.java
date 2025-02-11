package com.project.cheerha.domain.keyword.dto.response;

import java.util.List;

public record CreateUserKeywordResponseDto(List<Long> keywordIdList) {

    public static CreateUserKeywordResponseDto of(List<Long> keywordIdList) {
        return new CreateUserKeywordResponseDto(keywordIdList);
    }
}
