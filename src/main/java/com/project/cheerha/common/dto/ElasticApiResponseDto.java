package com.project.cheerha.common.dto;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;

public record ElasticApiResponseDto<T>(LocalDateTime responseAt, boolean isSuccessful, T data, String message) {
    /**
     * @param isSuccessful 응답의 성공 여부 (true: 성공, false: 실패)
     * @param data 응답 데이터
     * @param message 응답 메시지
     */
    public ElasticApiResponseDto(boolean isSuccessful, T data, String message) {
        this(LocalDateTime.now(ZoneId.of("Asia/Seoul")), isSuccessful, data, message);
    }

    public static <T> ResponseEntity<ElasticApiResponseDto<T>> success(T data, String message) {
        return ResponseEntity.ok(new ElasticApiResponseDto<>(true, data, message));
    }
}