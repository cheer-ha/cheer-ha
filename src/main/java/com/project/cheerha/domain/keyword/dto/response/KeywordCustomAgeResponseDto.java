package com.project.cheerha.domain.keyword.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class KeywordCustomAgeResponseDto {
    private final Long id;
    private final String keyword;
    private final Long keywordCount;

    @QueryProjection
    public KeywordCustomAgeResponseDto(Long id, String keyword, Long keywordCount) {
        this.id = id;
        this.keyword = keyword;
        this.keywordCount = keywordCount;
    }
}
