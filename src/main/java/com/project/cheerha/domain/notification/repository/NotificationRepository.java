package com.project.cheerha.domain.notification.repository;

import com.project.cheerha.domain.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * 특정 이메일과 채용 공고 URL 목록에 해당하는 Mapping 목록 조회
     *
     * @param email 조회할 사용자 이메일
     * @param jobOpeningUrlList 조회할 채용 공고 URL 목록
     * @return 해당 이메일과 채용 공고 URL이 연결된 Mapping 목록
     */
    List<Notification> findAllByEmailAndJobOpeningUrlIn(
        String email,
        List<String> jobOpeningUrlList
    );

    /**
     * 아직 이메일이 발송되지 않은 Mapping 목록 조회
     *
     * @return 이메일이 발송되지 않은 Mapping 목록
     */
    List<Notification> findByIsEmailSentFalse();
}