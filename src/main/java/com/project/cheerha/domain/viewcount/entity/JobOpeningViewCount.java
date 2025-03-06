package com.project.cheerha.domain.viewcount.entity;

import com.project.cheerha.domain.jobopening.entity.JobOpening;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "job_opening_view_count")
@EntityListeners(AuditingEntityListener.class)
public class JobOpeningViewCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int viewCount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_opening_id", nullable = false,unique = true)
    private JobOpening jobOpening;

    public JobOpeningViewCount(JobOpening jobOpening, int viewCount) {
        this.jobOpening = jobOpening;
        this.viewCount = viewCount;
    }

    public static JobOpeningViewCount create(JobOpening jobOpening) {
        return new JobOpeningViewCount(jobOpening, 0); // 기본 조회수 0
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}
