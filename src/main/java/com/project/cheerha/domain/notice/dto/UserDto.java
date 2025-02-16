package com.project.cheerha.domain.notice.dto;

import com.querydsl.core.annotations.QueryProjection;

public record UserDto(Long userId, String email) {

    @QueryProjection
    public UserDto {
    }
}
