package com.project.cheerha.domain.bookmark.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ReadBookmarkAgeRequestDto(
    /**
     * 사용자가 커스터마이징하는 나이는 최소 20세에서 최대 79세까지 가능
     * todo:키워드와 Request 내용이 완전히 동일한데 한 개로 합치면서 쓰기 편할만한 방법 있을까욥
     */
    @Min(value = 20, message = "최소 나이는 20세 이상이어야 합니다.")
    @Max(value = 79, message = "최소 나이는 79세 이하여야 합니다.")
    int minAge,

    @Min(value = 20, message = "최대 나이는 20세 이상이어야 합니다.")
    @Max(value = 79, message = "최대 나이는 79세 이하여야 합니다.")
    int maxAge
) {

}
