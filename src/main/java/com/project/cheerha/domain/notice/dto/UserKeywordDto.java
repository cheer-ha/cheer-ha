package com.project.cheerha.domain.notice.dto;

import com.querydsl.core.annotations.QueryProjection;

public record UserKeywordDto(
    Long userId,
    Long keywordId,
    String email
) {

    @QueryProjection
    public UserKeywordDto {
    }
}