package com.project.cheerha.domain.jobOpening.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

// TODO : 우선은 와이어프레임에 나오는 데이터만 반환되도록 설정, 보여줘야 하는 데이터가 더 많아진다면 추가해야 함
@Getter
public class ReadJobOpeningResponseDto {

    private final Long id;
    private final String company;
    private final ZonedDateTime hiringStartAt;
    private final ZonedDateTime hiringEndAt;
    private final String position;

    private List<String> requiredSkills;

    @QueryProjection
    public ReadJobOpeningResponseDto(
            Long id, String company,
            ZonedDateTime hiringStartAt, ZonedDateTime hiringEndAt,
            String position
    ) {
        this.id = id;
        this.company = company;
        this.hiringStartAt = hiringStartAt;
        this.hiringEndAt = hiringEndAt;
        this.position = position;
    }

    public void addRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }
}
