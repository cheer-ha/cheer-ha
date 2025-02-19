package com.project.cheerha.domain.notice.dto.refactor;

import com.querydsl.core.annotations.QueryProjection;


public record UserKeywordDto(
    Long keywordId,
    String email
) {

    @QueryProjection
    public UserKeywordDto(
        Long keywordId,
        String email
    ) {
        this.keywordId = keywordId;
        this.email = email;
    }
}