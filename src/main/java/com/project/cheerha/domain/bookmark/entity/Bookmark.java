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
)
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

    /**
     * 사용자가 북마크할 채용 공고와 사용자 정보를 받아서,
     * 새로운 북마크 엔티티 객체를 생성하는 메서드입니다.
     *
     * @param user 사용자의 정보
     * @param jobOpening 채용 공고의 정보
     * @return 생성된 북마크 엔티티 객체
     */
    public static Bookmark toEntity(User user, JobOpening jobOpening) {
        Bookmark bookmark = new Bookmark();
        bookmark.user = user;
        bookmark.jobOpening = jobOpening;
        return bookmark;
    }
}
