package com.project.cheerha.domain.data.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

// TODO : 우선은 와이어프레임에 나오는 데이터만 반환되도록 설정, 보여줘야 하는 데이터가 더 많아진다면 추가해야 함
@Getter
public class ReadDataResponseDto {

    private final Long id;
    private final String company;
    private final LocalDateTime hiringStartsAt;
    private final LocalDateTime hiringEndsAt;
    private final String position;

    private List<String> requiredSkills;

    @QueryProjection
    public ReadDataResponseDto(
            Long id, String company,
            LocalDateTime hiringStartsAt, LocalDateTime hiringEndsAt,
            String position
    ) {
        this.id = id;
        this.company = company;
        this.hiringStartsAt = hiringStartsAt;
        this.hiringEndsAt = hiringEndsAt;
        this.position = position;
    }

    public void addRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }
}
