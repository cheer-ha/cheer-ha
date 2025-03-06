package com.project.cheerha.domain.keyword.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record KeywordCustomAgeResponseDto (
        Long id,
        String keyword,
        Long keywordCount
) {
    @QueryProjection
    public KeywordCustomAgeResponseDto {
    }
}
