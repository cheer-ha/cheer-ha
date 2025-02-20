package com.project.cheerha.domain.bookmark.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class BookmarkCustomAgeResponseDto {

    private final Long id;
    private final String title;
    private final String company;
    private final Enum educationLevel;
    private final Enum employmentType;
    private final ZonedDateTime hiringStartAt;
    private final ZonedDateTime hiringEndAt;
    private final String position;
    private final int salary;
    private final int minExperienceYears;
    private final int maxExperienceYears;
    private final Long bookmarkCount;

    @QueryProjection
    public BookmarkCustomAgeResponseDto(
        Long bookmarkId,
        String title,
        String company,
        Enum educationLevel,
        Enum employmentType,
        ZonedDateTime hiringStartAt,
        ZonedDateTime hiringEndAt,
        String position,
        int salary,
        int minExperienceYears,
        int maxExperienceYears,
        Long bookmarkCount
    ) {

        this.id = bookmarkId;
        this.title = title;
        this.company = company;
        this.educationLevel = educationLevel;
        this.employmentType = employmentType;
        this.hiringStartAt = hiringStartAt;
        this.hiringEndAt = hiringEndAt;
        this.position = position;
        this.salary = salary;
        this.minExperienceYears = minExperienceYears;
        this.maxExperienceYears = maxExperienceYears;
        this.bookmarkCount = bookmarkCount;
    }
}
