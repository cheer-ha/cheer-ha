package com.project.cheerha.domain.notice.dto;

import com.querydsl.core.annotations.QueryProjection;

/**
 * 스케줄러에 채용 공고 관련 데이터를 가져오는 데 필요한 DTO
 * @param jobOpeningId 채용 공고 ID
 * @param keywordId 키워드 ID
 * @param url 채용 공고의 URL
 */
public record JobOpeningKeywordDto(
    Long jobOpeningId,
    Long keywordId,
    String url
) {

    @QueryProjection
    public JobOpeningKeywordDto(
        Long jobOpeningId,
        Long keywordId,
        String url
    ) {
        this.jobOpeningId = jobOpeningId;
        this.keywordId = keywordId;
        this.url = url;
    }
}