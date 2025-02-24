package com.project.cheerha.domain.notice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(
    name = "email_job_opening_mapping",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "jobOpeningUrl"})
    })
public class EmailJobOpeningMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String jobOpeningUrl;

    /**
     * 이메일과 채용 공고 URL을 받아 EmailJobOpeningMapping 객체 생성
     * @param email 사용자의 이메일 주소
     * @param jobOpeningUrl 채용 공고 URL
     * @return 생성된 EmailJobOpeningMapping 객체
     */
    public static EmailJobOpeningMapping toEntity(
        String email,
        String jobOpeningUrl
    ) {
        EmailJobOpeningMapping emailJobOpeningMapping = new EmailJobOpeningMapping();
        emailJobOpeningMapping.email = email;
        emailJobOpeningMapping.jobOpeningUrl = jobOpeningUrl;
        return emailJobOpeningMapping;
    }
}