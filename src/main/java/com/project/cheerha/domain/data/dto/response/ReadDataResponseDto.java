package com.project.cheerha.domain.data.dto.response;

import com.project.cheerha.domain.data.entity.Data;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

// TODO : 우선은 와이어프레임에 나오는 데이터만 반환되도록 설정, 보여줘야 하는 데이터가 더 많아진다면 추가해야 함
@Getter
public class ReadDataResponseDto {

    private final String company;
    private final LocalDateTime hiringStartsAt;
    private final LocalDateTime hiringEndsAt;
    private final String position;
    private final List<String> requiredSkills;

    @QueryProjection
    public ReadDataResponseDto(
            String company, LocalDateTime hiringStartsAt,
            LocalDateTime hiringEndsAt, String position,
            List<String> requiredSkills
    ) {
        this.company = company;
        this.hiringStartsAt = hiringStartsAt;
        this.hiringEndsAt = hiringEndsAt;
        this.position = position;
        this.requiredSkills = requiredSkills;
    }

    public static ReadDataResponseDto toDto(Data data) {
        return new ReadDataResponseDto(
                data.getCompany(),
                data.getHiringStartPeriod(),
                data.getHiringEndPeriod(),
                data.getPosition(),
                data.getRequiredSkills());
    }
}
