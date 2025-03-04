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
    name = "notification",
    uniqueConstraints = {@UniqueConstraint(
        columnNames = {"email", "job_opening_url"}
    )}
)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String jobOpeningUrl;

    @Column
    private boolean isEmailSent;

    @Column
    private boolean isPushSent;

    /**
     * Mapping 객체를 생성하는 정적 팩토리 메서드
     *
     * @param email         : 사용자 이메일
     * @param jobOpeningUrl : 채용 공고 URL
     * @return 생성된 Mapping 객체
     * <p>
     * 기본값 1: 이메일 미발송 기본값 2: 푸시 미발송
     */
    public static Notification toEntity(
        String email,
        String jobOpeningUrl
    ) {
        Notification notification = new Notification();
        notification.email = email;
        notification.jobOpeningUrl = jobOpeningUrl;
        notification.isEmailSent = false;
        notification.isPushSent = false;
        return notification;
    }

    // 이메일 발송 상태를 '발송됨'으로 변경
    public void markEmailAsSent() {
        this.isEmailSent = true;
    }

    // 푸시 발송 상태를 '발송됨'으로 변경
    public void markPushAsSent() {
        this.isPushSent = true;
    }
}