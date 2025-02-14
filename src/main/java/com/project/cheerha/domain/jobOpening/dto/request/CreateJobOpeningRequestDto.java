package com.project.cheerha.domain.jobOpening.dto.request;

import java.time.ZonedDateTime;
import java.util.List;

public record CreateJobOpeningRequestDto(
        String title,
        String company,
        String location,
        Integer salary,
        String employmentType,
        String educationLevel,
        String jobOpeningUrl,
        Integer minExperienceYears,
        Integer maxExperienceYears,
        String position,
        ZonedDateTime hiringStartAt,
        ZonedDateTime hiringEndAt,
        List<String> jobOpeningKeywordList
) {

}