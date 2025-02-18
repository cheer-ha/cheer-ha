package com.project.cheerha.domain.bookmark.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * 북마크 삭제 요청을 위한 DTO 클래스입니다.
 * 사용자가 삭제할 채용 공고의 ID를 포함하여 북마크 삭제 요청을 처리합니다.
 */
public record DeleteBookmarkRequestDto(
    @NotNull
    Long jobOpeningId
) {

}