package com.project.cheerha.domain.history.dto.response;

import com.project.cheerha.domain.history.entity.History;

import java.time.LocalDateTime;

public record ReadHistoryResponseDto(String name, LocalDateTime createdAt) {

    public static ReadHistoryResponseDto toDto(History history) {
        return new ReadHistoryResponseDto(history.getName(), history.getCreatedAt());
    }
}
