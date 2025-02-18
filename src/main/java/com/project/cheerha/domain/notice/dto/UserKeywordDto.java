package com.project.cheerha.domain.notice.dto;

import com.querydsl.core.annotations.QueryProjection;

/**
 * 스케줄러에 사용자 관련 데이터를 가져오는 데 필요한 DTO
 * @param userId 사용자 ID
 * @param keywordId 사용자가 고른 키워드 ID
 * @param email 사용자의 이메일
 */
public record UserKeywordDto(
    Long userId,
    Long keywordId,
    String email
) {

    @QueryProjection
    public UserKeywordDto(
        Long userId,
        Long keywordId,
        String email
    ) {
        this.userId = userId;
        this.keywordId = keywordId;
        this.email = email;
    }
}