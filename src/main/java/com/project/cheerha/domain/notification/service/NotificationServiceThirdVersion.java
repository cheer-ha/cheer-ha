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
public class NotificationServiceThirdVersion {

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

    private Map<String, Map<String, Long>> compareKeywordOverlap(
        Map<String, Set<Long>> emailToKeywordIdSet,
        Map<String, Set<Long>> urlToKeywordIdSet
    ) {
        return emailToKeywordIdSet.entrySet().stream()  // 이메일과 해당 이메일에 대응하는 키워드 목록을 처리하고자 entrySet을 스트림으로 변환
            .collect(Collectors.toMap(  // 스트림의 결과를 Map으로 수집
                Map.Entry::getKey,  // Map의 key는 이메일 주소, 즉 entry.getKey()는 이메일
                entry -> urlToKeywordIdSet.keySet().stream()  // URL들을 순회하고자 urlToKeywordIdSet에서 키(즉, URL)를 스트림으로 변환
                    .collect(Collectors.toMap(  // 각 URL에 계산된 겹치는 키워드 개수를 Map으로 수집
                        url -> url,  // URL이 Map의 키
                        url -> entry.getValue().stream()  // 이메일에 대응하는 키워드 목록을 스트림으로 변환
                            .filter(urlToKeywordIdSet.get(url)::contains)  // 해당 URL과 겹치는 키워드를 찾고자 URL에 대응하는 키워드 목록과 비교
                            .count()  // 겹치는 키워드 개수를 셈
                    ))
            ));
    }
}