package com.project.cheerha.domain.notice;

import com.project.cheerha.domain.notice.dto.JobOpeningKeywordDto;
import com.project.cheerha.domain.notice.dto.UserKeywordDto;
import com.project.cheerha.domain.notice.service.EmailService;
import com.project.cheerha.domain.notice.service.EmailFindService;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSchedulerLv0 {

    private final EmailFindService emailFindService;
    private final EmailService emailService;

    // 30초마다 스케줄러 작동
    // 30초마다 채용 공고를 조회하여 이메일을 전송하는 스케줄러
    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void sendJobOpeningMatchingNotices() {
        Instant startTime = Instant.now();

        // 중복 조회를 방지하고자 조회 시간 사용
        ZonedDateTime referenceTime = ZonedDateTime.now().minusDays(2L)
            .withZoneSameInstant(ZoneId.of("UTC"));

        Instant afterReferenceTime = Instant.now();
        log.info("기준 시간 설정 완료 (소요 시간: {} ms)",
            Duration.between(startTime, afterReferenceTime).toMillis());

        // 채용 공고의 ID, 키워드 ID, 채용 공고의 URL 조회
        List<JobOpeningKeywordDto> jobOpeningKeywordDtoList = emailFindService.findAllJobOpeningKeywords(
            referenceTime);

        Instant afterJobKeywordFetch = Instant.now();
        log.info("채용 공고 키워드 조회 완료 (소요 시간: {} ms)",
            Duration.between(afterReferenceTime, afterJobKeywordFetch).toMillis());

        // 사용자의 ID, 키워드 ID, 사용자의 이메일 조회
        List<UserKeywordDto> userKeywordDtoList = emailFindService.findAllUserKeywords();
        Instant afterUserKeywordFetch = Instant.now();
        log.info("사용자 키워드 조회 완료 (소요 시간: {} ms)",
            Duration.between(afterJobKeywordFetch, afterUserKeywordFetch).toMillis());

        log.info("채용 공고 목록: {}", jobOpeningKeywordDtoList);
        log.info("사용자 목록: {}", userKeywordDtoList);

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
        Instant afterMatching = Instant.now();
        log.info("매칭 로직 수행 완료 (소요 시간: {} ms)",
            Duration.between(afterUserKeywordFetch, afterMatching).toMillis());

        // (5) 이메일별로 알림 전송
        // todo 이메일 검증 필요함
        emailUrlMap.forEach((email, urlSet) -> {
                Instant emailStartTime = Instant.now(); // 이메일 전송 시작 시간

                log.info("사용자 이메일: {} - 매칭된 채용 공고 URL 목록: {}", email, urlSet);

                emailService.sendMail(email, List.copyOf(urlSet));

                Instant emailEndTime = Instant.now();
                Duration emailDuration = Duration.between(emailStartTime, emailEndTime);
                log.info("개별 이메일 전송 완료: {} (소요 시간: {} s {} ms)", email, emailDuration.toSeconds(),
                    emailDuration.toMillisPart());
            }
        );
        Instant afterEmailSend = Instant.now();

        Duration emailSendDuration = Duration.between(afterMatching, afterEmailSend);
        Duration totalDuration = Duration.between(startTime, afterEmailSend);

        log.info("전체 이메일 전송 완료 (소요 시간: {} s {} ms)", emailSendDuration.toSeconds(),
            emailSendDuration.toMillisPart());
        log.info("전체 작업 완료 (총 소요 시간: {} s {} ms)", totalDuration.toSeconds(),
            totalDuration.toMillisPart());
    }
}