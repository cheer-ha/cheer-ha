package com.project.cheerha.domain.bookmark.entity;

import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import com.project.cheerha.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "bookmark", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "job_opening_id"})}
)  // user_id와 job_opening_id의 복합 유니크 제약)
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_opening_id")
    private JobOpening jobOpening;

    public Bookmark(JobOpening jobOpening) {
        this.jobOpening = jobOpening;
    }

    public Bookmark(User user, JobOpening jobOpening) {
        this.user = user;
        this.jobOpening = jobOpening;
    }

    // userId로 User를 찾아서 Bookmark 엔티티 생성
    public static Bookmark toEntity(User user, JobOpening jobOpening) {
        return new Bookmark(user, jobOpening);  // user와 jobOpening을 이용해 Bookmark 엔티티 생성
    }
}
