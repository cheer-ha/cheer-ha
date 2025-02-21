package com.project.cheerha.domain.keyword.entity;

import com.project.cheerha.domain.jobopening.entity.JobOpening;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

@Entity
@Getter
@Table(name = "job_opening_keyword", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"keyword_id", "job_opening_id"})}
)
public class JobOpeningKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    @ManyToOne
    @JoinColumn(name = "job_opening_id")
    private JobOpening jobOpening;

    public static JobOpeningKeyword toEntity(JobOpening jobOpening, Keyword keyword) {
        JobOpeningKeyword jobOpeningKeyword = new JobOpeningKeyword();
        jobOpeningKeyword.keyword = keyword;
        jobOpeningKeyword.jobOpening = jobOpening;
        return jobOpeningKeyword;
    }
}
