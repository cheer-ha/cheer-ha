package com.project.cheerha.domain.data.dto;

import java.time.LocalDate;
import java.util.List;

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
        List<String> requiredSkillList
) {

}