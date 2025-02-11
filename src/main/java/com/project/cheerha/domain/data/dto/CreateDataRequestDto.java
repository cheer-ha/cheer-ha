package com.project.cheerha.domain.data.dto;

import com.project.cheerha.domain.data.entity.Data;
import com.project.cheerha.domain.keyword.entity.DataKeyword;

import java.time.LocalDate;
import java.util.Set;

public record CreateDataRequestDto(
        String title,
        String company,
        String location,
        Integer salary,
        String jobType,
        String url,
        LocalDate hiringStartPeriod,
        LocalDate hiringEndPeriod,
        String education,
        Integer career,
        String position,
        Set<String> requiredSkills // 추가된 필드: 자격 요건
) {
    // CreateDataRequestDto에서 Data 엔티티를 생성하는 팩토리 메서드
    public Data toEntity(Set<DataKeyword> dataKeywords) {
        return new Data(
                title,
                company,
                location,
                salary,
                jobType,
                url,
                hiringStartPeriod,
                hiringEndPeriod,
                education,
                career,
                position,
                dataKeywords
        );
    }

    // DTO에서 Data 엔티티로 변환하는 팩토리 메서드
    public static Data fromDto(CreateDataRequestDto dto, Set<DataKeyword> dataKeywords) {
        return new Data(
                dto.title(),  // getter 없이 바로 필드 이름 사용
                dto.company(),
                dto.location(),
                dto.salary(),
                dto.jobType(),
                dto.url(),
                dto.hiringStartPeriod(),
                dto.hiringEndPeriod(),
                dto.education(),
                dto.career(),
                dto.position(),
                dataKeywords // DataKeyword와의 연결
        );
    }
}
