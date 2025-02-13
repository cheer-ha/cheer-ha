package com.project.cheerha.domain.bookmark.dto;

import jakarta.validation.constraints.NotNull;

public record DeleteBookmarkRequestDto(
        @NotNull
        Long jobOpeningId
) {

}