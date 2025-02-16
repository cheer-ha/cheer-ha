package com.project.cheerha.domain.notice.dto;

import com.querydsl.core.annotations.QueryProjection;

public record JobOpeningDto(Long jobOpeningId, String url) {

    @QueryProjection
    public JobOpeningDto {

    }
}
