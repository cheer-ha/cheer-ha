package com.project.cheerha.domain.notification.service;

import com.project.cheerha.domain.notification.dto.NotificationRecipientDto;
import com.project.cheerha.domain.notification.entity.Notification;
import com.project.cheerha.domain.notification.repository.NotificationRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * 알림(Notification) 객체를 생성하여 저장하는 메서드
     * @param notificationRecipientDtoList 수신자의 이메일 및 키워드 ID 목록
     * @param keywordIdToUrlList key: 키워드 ID, value: 채용 공고 URL 목록
     */
    @Transactional
    public void createNotification(
        List<NotificationRecipientDto> notificationRecipientDtoList,
        Map<Long, List<String>> keywordIdToUrlList
    ) {
        Map<String, Set<String>> emailToUrlSet = new HashMap<>();

        // 사용자의 키워드 ID를 기반으로 이메일별 채용 공고 URL 매칭
        matchEmailToUrlSetByKeywordId(
            notificationRecipientDtoList,
            keywordIdToUrlList,
            emailToUrlSet
        );

        // 이메일별로 알림 객체 생성 및 저장
        emailToUrlSet.forEach((email, urlSet) -> {

            // 기존에 저장된 알림 조회
            List<Notification> foundNotificationList = notificationRepository.findAllByEmailAndJobOpeningUrlIn(
                email,
                urlSet.stream().toList()
            );

            // 이미 존재하는 채용 공고 URL 추출
            Set<String> existingUrlSet = findExistingUrlSet(foundNotificationList);

            // 새로운 알림 객체 생성
            List<Notification> notificationList = createNotificationList(
                email,
                urlSet,
                existingUrlSet
            );

            // 알림 저장
            notificationRepository.saveAll(notificationList);
        });
    }

    /**
     * 키워드 ID가 동일한 사용자 이메일과 채용 공고 URL을 매칭하는 메서드
     * @param notificationRecipientDtoList 수신자의 이메일 및 키워드 ID 목록
     * @param keywordIdToUrlList key: 키워드 ID, value: 채용 공고 URL 목록
     * @param emailToUrlSet key: 이메일, valye: 해당 이메일 주소로 받을 채용 공고 URL 집합
     */
    private void matchEmailToUrlSetByKeywordId(
        List<NotificationRecipientDto> notificationRecipientDtoList,
        Map<Long, List<String>> keywordIdToUrlList,
        Map<String, Set<String>> emailToUrlSet
    ) {
        for (NotificationRecipientDto dto : notificationRecipientDtoList) {
            List<String> matchingUrlList = keywordIdToUrlList.getOrDefault(
                dto.keywordId(),
                List.of()
            );

            if (!matchingUrlList.isEmpty()) {
                emailToUrlSet.computeIfAbsent(
                    dto.email(),
                    email -> new HashSet<>()
                ).addAll(matchingUrlList);
            }
        }
    }

    /**
     * 기존에 저장된 알림에서 채용 공고 URL 목록을 조회하는 메서드
     * @param notificationList 기존에 저장된 알림 목록
     * @return 기존에 저장된 채용 공고 URL 집합
     */
    private Set<String> findExistingUrlSet(List<Notification> notificationList) {
        return notificationList.stream()
            .map(Notification::getJobOpeningUrl)
            .collect(Collectors.toSet());
    }

    /**
     * 새로운 알림 목록을 생성하는 메서드
     * @param email 알림을 받을 사용자의 이메일
     * @param urlSet 사용자가 고른 키워드로 매칭된 채용 공고 URL 집합
     * @param existingUrlSet 이미 저장된 채용 공고 URL 집합
     * @return 새로운 알림(Notification) 목록
     */
    private List<Notification> createNotificationList(
        String email,
        Set<String> urlSet,
        Set<String> existingUrlSet
    ) {
        return urlSet.stream()
            .filter(jobOpeningUrl -> !existingUrlSet.contains(jobOpeningUrl)) // 기존 알림에 없는 URL 필터링
            .map(jobOpeningUrl -> Notification.toEntity(email, jobOpeningUrl)) // 알림 객체 생성
            .toList();
    }
}