package com.project.cheerha.domain.keyword.dto.response;

import com.project.cheerha.domain.keyword.entity.UserKeyword;

public record CreateUserKeywordResponseDto(Long keywordId) {

    public static CreateUserKeywordResponseDto of(UserKeyword userKeyword) {
        return new CreateUserKeywordResponseDto(userKeyword.getKeyword().getId());
    }
}
