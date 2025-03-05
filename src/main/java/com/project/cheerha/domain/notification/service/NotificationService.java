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
     * 알림(Notification) 생성
     *
     * @param notificationRecipientDtoList : 알림 받을 사용자 및 해당 사용자의 키워드 ID를 포함한 DTO 목록
     * @param keywordIdToUrlList           : key가 키워드 ID, value가 URL 목록인 map
     */
    @Transactional
    public void createNotification(
        List<NotificationRecipientDto> notificationRecipientDtoList,
        Map<Long, List<String>> keywordIdToUrlList
    ) {
        // key: 이메일 주소, value: 채용 공고 URL Set
        Map<String, Set<String>> emailToUrlSet = new HashMap<>();

        // 겹치는 키워드 ID로 이메일 주소와 URL Set 매칭
        matchEmailToUrlSetByKeywordId(notificationRecipientDtoList, keywordIdToUrlList,
            emailToUrlSet);

        // 이메일 주소별로 알림 생성 및 저장
        emailToUrlSet.forEach((email, urlSet) -> {

            // 이미 존재하는 알림 객체 조회
            List<Notification> foundNotificationList = notificationRepository.findAllByEmailAndJobOpeningUrlIn(
                email, urlSet.stream().toList());

            // 이미 존재하는 URL Set 조회
            Set<String> existingUrlSet = findExistingUrlSet(foundNotificationList);

            // 이미 존재하는지 검증 후 알림 객체 생성
            List<Notification> notificationList = createNotificationList(email, urlSet,
                existingUrlSet);

            // 알림 목록을 한꺼번에 저장
            notificationRepository.saveAll(notificationList);
        });
    }

    /**
     * 겹치는 키워드 ID로 이메일 주소와 채용 공고 URL 매칭
     *
     * @param notificationRecipientDtoList : 알림을 받을 사용자 목록
     * @param keywordIdToUrlList           : key: 키워드 ID, value: URL 목록
     * @param emailToUrlSet                : 이메일 주소 key로 사용해 URL Set을 추가하는 Map
     */
    private void matchEmailToUrlSetByKeywordId(
        List<NotificationRecipientDto> notificationRecipientDtoList,
        Map<Long, List<String>> keywordIdToUrlList,
        Map<String, Set<String>> emailToUrlSet
    ) {
        // 각 사용자가 고른 키워드 ID와 일치하는 URL 목록 조회
        for (NotificationRecipientDto dto : notificationRecipientDtoList) {
            List<String> matchingUrlList = keywordIdToUrlList.getOrDefault(dto.keywordId(), List.of());

            // 키워드 ID에 해당하는 URL 목록이 있으면 추가
            if (!matchingUrlList.isEmpty()) {
                emailToUrlSet.computeIfAbsent(
                    dto.email(),
                    email -> new HashSet<>()
                ).addAll(matchingUrlList);
            }
        }
    }

    /**
     *  이미 존재하는 URL을 Set으로 변환
     * @param notificationList : 기존에 존재하는 알림 목록
     * @return 알림에서 추출한 채용 공고 URL Set
     */
    private Set<String> findExistingUrlSet(List<Notification> notificationList) {
        return notificationList.stream()
            .map(Notification::getJobOpeningUrl)
            .collect(Collectors.toSet());
    }

    /**
     * 이미 존재하는 URL을 제외하고 새로운 알림 객체 목록 생성
     * @param email : 알림을 받을 사용자 이메일
     * @param urlSet : 알림에 들어갈 채용 공고 URL Set
     * @param existingUrlSet : 이미 존재하는 URL Set
     * @return 새로 생성할 알림 객체 목록
     */
    private List<Notification> createNotificationList(String email, Set<String> urlSet, Set<String> existingUrlSet) {
        return urlSet.stream()
            .filter(jobOpeningUrl -> !existingUrlSet.contains(jobOpeningUrl))
            .map(jobOpeningUrl -> Notification.toEntity(email, jobOpeningUrl))
            .toList();
    }
}