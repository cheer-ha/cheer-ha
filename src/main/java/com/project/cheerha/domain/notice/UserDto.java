package com.project.cheerha.domain.notice;

import com.querydsl.core.annotations.QueryProjection;

public record UserDto (
    Long keywordId,
    String email
) {

    @QueryProjection
    public UserDto(
        Long keywordId,
        String email
    ) {
        this.keywordId = keywordId;
        this.email = email;
    }
}