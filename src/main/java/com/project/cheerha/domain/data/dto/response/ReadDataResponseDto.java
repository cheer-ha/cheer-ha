package com.project.cheerha.domain.data.dto.response;

import com.project.cheerha.domain.data.entity.Data;

import java.util.Set;

// TODO : 우선은 와이어프레임에 나오는 데이터만 반환되도록 설정, 보여줘야 하는 데이터가 더 많아진다면 추가해야 함
public record ReadDataResponseDto(
        String company,
        String hiringPeriod,
        String position,
        Set<String> requiredSkills
) {
    public static ReadDataResponseDto toDto(Data data) {
        return new ReadDataResponseDto(
                data.getCompany(),
                data.getHiringPeriod(),
                data.getPosition(),
                data.getRequiredSkills());
    }
}
