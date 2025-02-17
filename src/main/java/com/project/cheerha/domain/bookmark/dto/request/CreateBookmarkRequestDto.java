package com.project.cheerha.domain.bookmark.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * 북마크 생성 요청을 위한 DTO 클래스입니다.
 * 사용자가 새로운 북마크를 생성할 때, 채용 공고의 ID를 받아 처리합니다.
 */
public record CreateBookmarkRequestDto(
    @NotNull
    Long jobOpeningId
) {

}