package com.project.cheerha.domain.bookmark.dto.request;

import jakarta.validation.constraints.NotNull;

public record DeleteBookmarkRequestDto(
        @NotNull
        Long jobOpeningId
) {

}