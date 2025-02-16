package com.project.cheerha.domain.notice.dto;

import com.querydsl.core.annotations.QueryProjection;

public record JobOpeningKeywordDto(Long jobOpeningId, Long keywordId) {

    @QueryProjection
    public JobOpeningKeywordDto {

    }
}
