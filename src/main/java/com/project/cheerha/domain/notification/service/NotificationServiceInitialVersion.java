//package com.project.cheerha.domain.notification.service;
//
//import com.project.cheerha.domain.notification.dto.NotificationRecipientDto;
//import com.project.cheerha.domain.notification.entity.Notification;
//import com.project.cheerha.domain.notification.repository.NotificationRepository;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class NotificationServiceInitialVersion {
//
//    private final NotificationRepository notificationRepository;
//
//    @Transactional
//    public void createNotification(
//        List<NotificationRecipientDto> notificationRecipientDtoList,
//        Map<Long, List<String>> keywordIdToUrlList
//    ) {
//        Map<String, Set<String>> emailToUrlSet = new HashMap<>();
//
//        matchEmailToUrlSetByKeywordId(
//            notificationRecipientDtoList,
//            keywordIdToUrlList,
//            emailToUrlSet
//        );
//
//        emailToUrlSet.forEach((email, urlSet) -> {
//
//            List<Notification> foundNotificationList = notificationRepository.findAllByEmailAndJobOpeningUrlIn(
//                email,
//                urlSet.stream().toList()
//            );
//
//            Set<String> existingUrlSet = findExistingUrlSet(foundNotificationList);
//
//            List<Notification> notificationList = createNotificationList(
//                email,
//                urlSet,
//                existingUrlSet
//            );
//
//            notificationRepository.saveAll(notificationList);
//        });
//    }
//
//
//    private void matchEmailToUrlSetByKeywordId(
//        List<NotificationRecipientDto> notificationRecipientDtoList,
//        Map<Long, List<String>> keywordIdToUrlList,
//        Map<String, Set<String>> emailToUrlSet
//    ) {
//        for (NotificationRecipientDto dto : notificationRecipientDtoList) {
//            List<String> matchingUrlList = keywordIdToUrlList.getOrDefault(
//                dto.keywordId(),
//                List.of()
//            );
//
//            if (!matchingUrlList.isEmpty()) {
//                emailToUrlSet.computeIfAbsent(
//                    dto.email(),
//                    email -> new HashSet<>()
//                ).addAll(matchingUrlList);
//            }
//        }
//    }
//
//    private Set<String> findExistingUrlSet(List<Notification> notificationList) {
//        return notificationList.stream()
//            .map(Notification::getJobOpeningUrl)
//            .collect(Collectors.toSet());
//    }
//
//    private List<Notification> createNotificationList(
//        String email,
//        Set<String> urlSet,
//        Set<String> existingUrlSet
//    ) {
//        return urlSet.stream()
//            .filter(jobOpeningUrl -> !existingUrlSet.contains(jobOpeningUrl))
//            .map(jobOpeningUrl -> Notification.toEntity(email, jobOpeningUrl))
//            .toList();
//    }
//}