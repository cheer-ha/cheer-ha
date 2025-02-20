package com.project.cheerha.domain.jobOpening.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ReadJobOpeningElasticResponseDto 레코드는 Elasticsearch에서 조회한
 * 채용공고(Job Opening) 정보를 담는 DTO(Data Transfer Object)입니다.
 * 이 DTO는 클라이언트에 채용공고의 세부 정보를 전달하는 데 사용됩니다.
 */
public record ReadJobOpeningElasticResponseDto(
        String id, // 채용공고의 고유 ID
        String title, // 채용공고 제목
        String company, // 회사 이름
        String location, // 채용 지역
        Integer salary, // 연봉
        String employmentType, // 고용 형태 (예: 정규직, 계약직 등)
        String educationLevel, // 요구하는 학력 수준
        String jobOpeningUrl, // 채용공고 URL
        Integer minExperienceYears, // 최소 경력 연수
        Integer maxExperienceYears, // 최대 경력 연수
        String position, // 채용 직무
        LocalDateTime hiringStartAt, // 채용 시작일
        LocalDateTime hiringEndAt, // 채용 마감일
        Integer viewCount, // 조회수
        List<String> requiredSkills // 요구되는 기술 목록
) {

}
