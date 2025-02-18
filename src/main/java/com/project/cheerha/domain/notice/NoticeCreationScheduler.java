package com.project.cheerha.domain.notice;

import com.project.cheerha.domain.notice.dto.JobOpeningKeywordDto;
import com.project.cheerha.domain.notice.dto.UserKeywordDto;
import com.project.cheerha.domain.notice.service.EmailService;
import com.project.cheerha.domain.notice.service.NoticeCreationService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NoticeCreationScheduler {

    private final NoticeCreationService noticeCreationService;
    private final EmailService emailService;

    // 30초마다 스케줄러 작동
    // 30초마다 채용 공고를 조회하여 이메일을 전송하는 스케줄러
    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void sendJobOpeningMatchingNotices() {

        // 중복 조회를 방지하고자 조회 시간 사용
        ZonedDateTime referenceTime = ZonedDateTime.now().minusSeconds(30L)
            .withZoneSameInstant(ZoneId.of("UTC"));

        // 채용 공고의 ID, 키워드 ID, 채용 공고의 URL 조회
        List<JobOpeningKeywordDto> jobOpeningKeywordDtoList = noticeCreationService.findAllJobOpeningKeywords(
            referenceTime);

        // 사용자의 ID, 키워드 ID, 사용자의 이메일 조회
        List<UserKeywordDto> userKeywordDtoList = noticeCreationService.findAllUserKeywords();

        // 사용자의 이메일 및 연결된 채용 공고 URL을 저장할 Map
        Map<String, Set<String>> emailUrlMap = new HashMap<>();

        // (1) 사용자 키워드 ID 목록 순회
        for (UserKeywordDto userDto : userKeywordDtoList) {

            // (2) 모든 채용 공고 키워드 ID 목록 순회
            for (JobOpeningKeywordDto jobOpeningKeywordDto : jobOpeningKeywordDtoList) {

                // (3) 키워드끼리 일치하는지 확인
                boolean isUserKeywordMatchingJobKeyword = userDto.keywordId()
                    .equals(jobOpeningKeywordDto.keywordId());

                // (4) 일치하면 이메일과 채용 공고 URL을 Map에 저장
                if (isUserKeywordMatchingJobKeyword) {
                    emailUrlMap
                        .computeIfAbsent(
                            userDto.email(),
                            emailAsKey -> new HashSet<>()
                        ).add(jobOpeningKeywordDto.url());
                }
            }
        }
        // (5) 이메일별로 알림 전송
        // todo 30초마다 이메일 발송되지 않도록 주석 처리
        // todo 이메일 검증 필요함
//        emailUrlMap.forEach((email, urlSet) -> {
//                emailService.sendMail(email, List.copyOf(urlSet));
//            }
//        );
    }
}