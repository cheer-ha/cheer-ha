package com.project.cheerha.domain.bookmark.dto.response;

import com.project.cheerha.domain.jobopening.entity.EducationLevel;
import com.project.cheerha.domain.jobopening.entity.EmploymentType;
import com.querydsl.core.annotations.QueryProjection;
import java.time.ZonedDateTime;

public record BookmarkCustomAgeResponseDto(
         Long id,
         String title,
         String company,
         EducationLevel educationLevel,
         EmploymentType employmentType,
         ZonedDateTime hiringStartAt,
         ZonedDateTime hiringEndAt,
         String position,
         int salary,
         int minExperienceYears,
         int maxExperienceYears,
         Long bookmarkCount
) {
    @QueryProjection
    public BookmarkCustomAgeResponseDto{
    }
}
