package com.project.cheerha.domain.elasticsearch.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReadJobOpeningElasticRequestDto {

    private String educationLevel;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime hiringStartAt;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime hiringEndAt;

    private String location;
    private Integer minExperienceYears;
    private Integer maxExperienceYears;
    private String employmentType;
    private String requiredSkill;

    @NotBlank
    private String searchTerm;
}
