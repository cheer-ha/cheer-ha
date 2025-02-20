package com.project.cheerha.domain.keyword.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReadKeywordAgeRequestDto(
    /**
     * 사용자가 커스터마이징하는 나이는 최소 20세에서 최대 79세까지 가능
     */
    @NotBlank
    @Min(20)
    int minAge,
    @NotBlank
    @Max(79)
    int maxAge
) {

}
