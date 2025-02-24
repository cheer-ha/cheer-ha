package com.project.cheerha.domain.notice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(
    name = "mapping",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "job_opening_url"})})
public class Mapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String jobOpeningUrl;

    @Column
    private boolean isRead;

    @Column
    private ZonedDateTime readTime;

    public static Mapping toEntity(
        String email,
        String jobOpeningUrl
    ) {
        Mapping mapping = new Mapping();
        mapping.email = email;
        mapping.jobOpeningUrl = jobOpeningUrl;
        mapping.isRead = false;
        return mapping;
    }
}