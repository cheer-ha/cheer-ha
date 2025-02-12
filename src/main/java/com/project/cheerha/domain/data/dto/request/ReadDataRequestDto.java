package com.project.cheerha.domain.data.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReadDataRequestDto {

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
