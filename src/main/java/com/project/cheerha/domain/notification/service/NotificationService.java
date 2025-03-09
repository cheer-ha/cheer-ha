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

    @Transactional
    public void createNotification(
        List<NotificationRecipientDto> notificationRecipientDtoList,
        Map<Long, List<String>> keywordIdToUrlList
    ) {
        Map<String, Set<String>> emailToUrlSet = new HashMap<>();

        matchEmailToUrlSetByKeywordId(
            notificationRecipientDtoList,
            keywordIdToUrlList,
            emailToUrlSet
        );

        Map<String, Map<String, Long>> emailToUrlToOverlapCount = compareKeywordOverlap(
            invertEmailToKeywordIdList(notificationRecipientDtoList),
            invertKeywordIdToUrlList(keywordIdToUrlList)
        );

        emailToUrlSet.forEach((email, urlSet) -> {

            List<Notification> foundNotificationList = notificationRepository.findAllByEmailAndJobOpeningUrlIn(
                email,
                urlSet.stream().toList()
            );

            Set<String> existingUrlSet = findExistingUrlSet(foundNotificationList);

            List<Notification> notificationList = createNotificationList(
                email,
                urlSet,
                existingUrlSet,
                emailToUrlToOverlapCount
            );

            notificationRepository.saveAll(notificationList);
        });
    }

    private void matchEmailToUrlSetByKeywordId(
        List<NotificationRecipientDto> notificationRecipientDtoList,
        Map<Long, List<String>> keywordIdToUrlList,
        Map<String, Set<String>> emailToUrlSet
    ) {
        for (NotificationRecipientDto dto : notificationRecipientDtoList) {
            List<String> matchingUrlList = keywordIdToUrlList.getOrDefault(
                dto.keywordId(),
                List.of());

            if (!matchingUrlList.isEmpty()) {
                emailToUrlSet.computeIfAbsent(
                    dto.email(),
                    email -> new HashSet<>()
                ).addAll(matchingUrlList);
            }
        }
    }

    private Set<String> findExistingUrlSet(List<Notification> notificationList) {
        return notificationList.stream()
            .map(Notification::getJobOpeningUrl)
            .collect(Collectors.toSet());
    }

    private List<Notification> createNotificationList(String email, Set<String> urlSet,
        Set<String> existingUrlSet, Map<String, Map<String, Long>> emailToUrlToOverlapCount) {
        return urlSet.stream()
            .filter(jobOpeningUrl -> !existingUrlSet.contains(jobOpeningUrl))
            .map(jobOpeningUrl -> {
                long overlapCount = emailToUrlToOverlapCount.getOrDefault(email, new HashMap<>())
                    .getOrDefault(jobOpeningUrl, 0L);
                return Notification.toEntity(email, jobOpeningUrl, (int) overlapCount);
            }).collect(Collectors.toList());
    }

    // keywordIdToUrlList를 URL -> keywordId 목록으로 바꾸는 메서드
    private Map<String, Set<Long>> invertKeywordIdToUrlList(
        Map<Long, List<String>> keywordIdToUrlList) {
        Map<String, Set<Long>> urlToKeywordIdSet = new HashMap<>();

        keywordIdToUrlList.forEach((keywordId, urlList) -> {
            for (String url : urlList) {
                urlToKeywordIdSet
                    .computeIfAbsent(url, urlAsKey -> new HashSet<>())
                    .add(keywordId);
            }
        });

        return urlToKeywordIdSet;
    }

    // 이메일을 키로, 키워드 ID를 값으로 바꾸는 메서드
    private Map<String, Set<Long>> invertEmailToKeywordIdList(
        List<NotificationRecipientDto> notificationRecipientDtoList) {
        Map<String, Set<Long>> emailToKeywordIdSet = new HashMap<>();

        notificationRecipientDtoList.forEach(dto -> {
            emailToKeywordIdSet
                .computeIfAbsent(dto.email(), emailAsKey -> new HashSet<>())
                .add(dto.keywordId());
        });

        return emailToKeywordIdSet;
    }

    // 겹치는 키워드 숫자를 세는 로직
    private Map<String, Map<String, Long>> compareKeywordOverlap(
        Map<String, Set<Long>> emailToKeywordIdSet,
        Map<String, Set<Long>> urlToKeywordIdSet
    ) {
        Map<String, Map<String, Long>> emailToUrlToOverlapCount = new HashMap<>();

        Set<String> emailSet = emailToKeywordIdSet.keySet();
        Set<String> urlSet = urlToKeywordIdSet.keySet();

        for (String email : emailSet) {
            Set<Long> userKeywords = emailToKeywordIdSet.get(email);

            for (String url : urlSet) {
                Set<Long> jobOpeningKeywords = urlToKeywordIdSet.get(url);

                long overlapCount = userKeywords.stream()
                    .filter(jobOpeningKeywords::contains)
                    .count();

                if (overlapCount > 0) {
                    emailToUrlToOverlapCount
                        .computeIfAbsent(
                            email,
                            emailAsKey -> new HashMap<>()
                        ).put(url, overlapCount);
                }
            }
        }

        return emailToUrlToOverlapCount;
    }
}