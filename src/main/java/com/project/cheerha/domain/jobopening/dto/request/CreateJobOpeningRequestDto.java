package com.project.cheerha.domain.jobopening.dto.request;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * 채용 공고 생성 요청을 위한 DTO 클래스입니다.
 * 사용자가 채용 공고를 생성할 때 필요한 정보를 담고 있습니다.
 */
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