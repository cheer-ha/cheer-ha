package com.project.cheerha.domain.jobopening.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Document(indexName = "job-opening")
public class JobOpeningDocument {

    @Id
    @Field(type = FieldType.Keyword)  // Elasticsearch에서 keyword 타입을 사용
    private String id; // Elasticsearch ID 필드

    @Field(type = FieldType.Text)  // Text 타입으로 저장
    private String title; // 직무 제목

    @Field(type = FieldType.Text)  // Text 타입으로 저장
    private String company; // 회사 이름

    @Field(type = FieldType.Text)  // Text 타입으로 저장
    private String location; // 근무지

    @Field(type = FieldType.Integer)  // Integer 타입으로 저장
    private int salary; // 연봉

    @Field(type = FieldType.Text)  // Text 타입으로 저장
    private String employmentType; // 고용 형태

    @Field(type = FieldType.Text)  // Text 타입으로 저장
    private String educationLevel; // 교육 수준

    @Field(type = FieldType.Text)  // Text 타입으로 저장
    private String jobOpeningUrl; // 채용 공고 URL

    @Field(type = FieldType.Integer)  // Integer 타입으로 저장
    private Integer minExperienceYears; // 최소 경력 연수

    @Field(type = FieldType.Integer)  // Integer 타입으로 저장
    private Integer maxExperienceYears; // 최대 경력 연수

    @Field(type = FieldType.Text)  // Text 타입으로 저장
    private String position; // 직무

    @Field(type = FieldType.Date)  // Date 타입으로 저장
    private LocalDateTime hiringStartAt; // 채용 시작일

    @Field(type = FieldType.Date)  // Date 타입으로 저장
    private LocalDateTime hiringEndAt; // 채용 종료일

    @Field(type = FieldType.Date)  // Date 타입으로 저장
    private LocalDateTime createdAt = LocalDateTime.now(); // 채용공고 생성일

    @Field(type = FieldType.Integer)  // Integer 타입으로 저장
    private int viewCount; // 조회수

    @Field(type = FieldType.Keyword)  // List<String>의 경우 Keyword 타입 사용
    private List<String> requiredSkills = new ArrayList<>(); // 자격 요건 키워드 리스트

    public static JobOpeningDocument create(JobOpening jobOpening) {
        return new JobOpeningDocument(
                jobOpening.getId().toString(),
                jobOpening.getTitle(),
                jobOpening.getCompany(),
                jobOpening.getLocation(),
                jobOpening.getSalary(),
                jobOpening.getEmploymentType().name(),
                jobOpening.getEducationLevel().name(),
                jobOpening.getJobOpeningUrl(),
                jobOpening.getMinExperienceYears(),
                jobOpening.getMaxExperienceYears(),
                jobOpening.getPosition(),
                convertZonedDateTimeToLocalDateTime(jobOpening.getHiringStartAt()),
                convertZonedDateTimeToLocalDateTime(jobOpening.getHiringEndAt()),
                convertZonedDateTimeToLocalDateTime(jobOpening.getCreatedAt()),
                jobOpening.getViewCount(),
                jobOpening.getRequiredSkillList()
        );
    }
    private static LocalDateTime convertZonedDateTimeToLocalDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime != null ? zonedDateTime.toLocalDateTime() : null; // Convert or return null if zonedDateTime is null
    }
}
