package com.project.cheerha.domain.keyword.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;


public record KeywordCustomAgeResponseDto (
        Long id,
        String keyword,
        Long keywordCount
) {
    @QueryProjection
    public KeywordCustomAgeResponseDto(Long id, String keyword, Long keywordCount) {
        this.id = id;
        this.keyword = keyword;
        this.keywordCount = keywordCount;
    }
}
