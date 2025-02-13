package com.project.cheerha.domain.history.dto.response;

import java.time.LocalDateTime;

public record ReadHistoryResponseDto(String name, LocalDateTime createdAt) {

}
