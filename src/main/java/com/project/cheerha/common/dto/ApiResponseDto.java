package com.project.cheerha.common.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

/**
 * API 응답을 위한 DTO 클래스입니다.
 * 이 클래스는 API 응답의 표준 구조를 정의하며, 성공 여부, 데이터 및 타임스탬프를 포함합니다.
 */
public record ApiResponseDto<T>(LocalDateTime responseAt, boolean isSuccessful, T data) {

    /**
     * 생성자: isSuccessful, data를 입력받아 responseAt은 현재 시간으로 자동 설정합니다.
     *
     * @param isSuccessful 응답의 성공 여부 (true: 성공, false: 실패)
     * @param data 응답 데이터
     */
    public ApiResponseDto(boolean isSuccessful, T data) {
        this(LocalDateTime.now(), isSuccessful, data);
    }

    /**
     * 생성된 리소스에 대한 응답을 반환하는 메서드입니다.
     *
     * 주어진 데이터를 포함하여 HTTP 201 Created 상태 코드와 함께 응답을 반환합니다.
     *
     * @param data 생성된 리소스의 데이터
     * @return HTTP 201 Created 응답과 함께 생성된 데이터가 포함된 ApiResponseDto
     */
    public static <T> ResponseEntity<ApiResponseDto<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDto<>(true, data));
    }

    /**
     * 성공적인 요청에 대한 응답을 반환하는 메서드입니다.
     *
     * 주어진 데이터를 포함하여 HTTP 200 OK 상태 코드와 함께 응답을 반환합니다.
     *
     * @param data 요청 성공 시 반환할 데이터
     * @return HTTP 200 OK 응답과 함께 성공 데이터가 포함된 ApiResponseDto
     */
    public static <T> ResponseEntity<ApiResponseDto<T>> success(T data) {
        return ResponseEntity.ok(new ApiResponseDto<>(true, data));
    }

    /**
     * 데이터가 없거나, 삭제된 경우에 대한 응답을 반환하는 메서드입니다.
     *
     * HTTP 204 No Content 상태 코드와 함께 빈 응답을 반환합니다.
     *
     * @return HTTP 204 No Content 응답
     */
    public static <T> ResponseEntity<ApiResponseDto<Void>> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponseDto<>(true, null));
    }
}
