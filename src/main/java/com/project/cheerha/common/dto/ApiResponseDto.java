package com.project.cheerha.common.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ApiResponseDto<T>(boolean successOrFail, T data, String message) {


    public static <T> ResponseEntity<ApiResponseDto<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDto<>(true, data, ""));
    }

    public static <T> ResponseEntity<ApiResponseDto<T>> success(T data) {
        return ResponseEntity.ok(new ApiResponseDto<>(true, data, ""));
    }

    public static <T> ResponseEntity<ApiResponseDto<Void>> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponseDto<>(true, null, ""));
    }
}
