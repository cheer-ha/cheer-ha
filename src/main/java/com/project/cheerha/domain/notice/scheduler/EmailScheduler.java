//package com.project.cheerha.domain.notice.scheduler;
//
//import com.project.cheerha.domain.notice.UserDto;
//import com.project.cheerha.domain.notice.service.DataFetchService;
//import com.project.cheerha.domain.notice.service.EmailService;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class EmailScheduler {
//
//    private final DataFetchService dataFetchService;
//    private final EmailService emailService;
//
//    @Scheduled(cron = "*/30 * * * * *")
//    @Transactional
//    public void sendJobOpeningMatchingNotices() {
//
//        ZonedDateTime referenceTime = ZonedDateTime.now().minusDays(7L)
//            .withZoneSameInstant(ZoneId.of("UTC"));
//
//        Map<Long, List<String>> jobOpeningKeywordMap = dataFetchService.findKeywordIdToUrlList(referenceTime);
//
//        List<UserDto> userDtoList = dataFetchService.findUserDtoList();
//
//        Map<String, Set<String>> emailUrlMap = new HashMap<>();
//
//        for (UserDto dto : userDtoList) {
//            List<String> matchingUrlList = jobOpeningKeywordMap.getOrDefault(
//                dto.keywordId(),
//                List.of()
//            );
//
//            if (!matchingUrlList.isEmpty()) {
//                emailUrlMap.computeIfAbsent(
//                    dto.email(),
//                    email -> new HashSet<>()
//                ).addAll(matchingUrlList);
//            }
//        }
//
//        emailUrlMap.forEach((email, urlSet) -> {
//            log.info("매칭 결과: {} - {}", email, urlSet);
//            emailService.sendMail(email, urlSet);
//        });
//    }
//}