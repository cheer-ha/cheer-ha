package com.project.cheerha.domain.notice.service;

import com.project.cheerha.domain.notice.dto.UserDto;
import com.project.cheerha.domain.notice.entity.Notification;
import com.project.cheerha.domain.notice.repository.NotificationRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 사용자와 채용 공고 URL을 연결하는 Service
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void saveMappings(
        List<UserDto> userDtoList,
        Map<Long, List<String>> keywordIdToUrlList
    ) {

        // todo 메서드 단위로 나누기 private으로 추출한 번 해보기
        // todo 지금은 구현체를 쭉 읽어야 한다면, 추출하면 메서드를 읽으면서 흐름 파악 가능 -> 그리고 구현체가 궁금하면 맨 밑에 찾아볼 것임
        // todo 특수 로직이므로 다른 클래스로 뺄 필요는 없음

        // key: 사용자 이메일
        // value: 해당 이메일에 연결되는 채용 공고 URL 목록
        Map<String, Set<String>> emailToUrl = new HashMap<>();

        // 사용자 정보를 순회하며 이메일별로 URL 목록 구성
        for (UserDto dto : userDtoList) {
            List<String> matchingUrlList = keywordIdToUrlList
                .getOrDefault(
                    dto.keywordId(),
                    List.of()
                );

            // 기존에 존재하지 않으면 새로운 HashSet 생성 후 추가
            if (!matchingUrlList.isEmpty()) {
                emailToUrl.computeIfAbsent(
                    dto.email(),
                    email -> new HashSet<>()
                ).addAll(matchingUrlList);
            }
        }

        // 이메일별로 연결된 채용 공고 URL 목록 처리
        emailToUrl.forEach((email, urlSet) -> {

            // 기존에 저장된 Mapping 목록 조회
            List<Notification> foundNotificationList = notificationRepository.findAllByEmailAndJobOpeningUrlIn(
                email,
                urlSet.stream().toList()
            );

            // 이미 존재하는 Mapping의 URL 목록을 Set으로 변환
            // 중복을 확인할 때 사용
            Set<String> existingUrlSet = foundNotificationList.stream()
                .map(Notification::getJobOpeningUrl)
                .collect(Collectors.toSet());

            // 중복 확인 후 기존에 없는 Mapping 객체만 생성
            // .filter : 중복 제거
            // .map: Mapping 객체 생성
            List<Notification> notificationList = urlSet.stream()
                .filter(jobOpeningUrl ->
                    !existingUrlSet.contains(jobOpeningUrl)
                ).map(jobOpeningUrl ->
                    Notification.toEntity(email, jobOpeningUrl)
                ).toList();

            // 새로운 Mapping 객체를 한꺼번에 데이터베이스에 저장
            notificationRepository.saveAll(notificationList);
        });
    }
}