package com.project.cheerha.domain.jobOpening.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReadJobOpeningRequestDto {

    private String education;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime hiringStartPeriod;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime hiringEndPeriod;

    private String location;
    private Integer career;
    private String jobType;
    private String requiredSkill;
}
