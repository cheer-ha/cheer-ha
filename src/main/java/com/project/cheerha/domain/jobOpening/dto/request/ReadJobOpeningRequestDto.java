package com.project.cheerha.domain.jobOpening.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReadJobOpeningRequestDto {

    private String educationLevel;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime hiringStartAt;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime hiringEndAt;

    private String location;
    private Integer ExperienceYears;
    private String employmentType;
    private String requiredSkill;
}
