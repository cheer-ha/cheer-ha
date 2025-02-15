package com.project.cheerha.domain.notification;

import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import com.project.cheerha.domain.jobOpening.repository.JobOpeningRepository;
import com.project.cheerha.domain.keyword.repository.JobOpeningKeywordRepository;
import com.project.cheerha.domain.keyword.repository.UserKeywordRepository;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final JobOpeningRepository jobOpeningRepository;
    private final JobOpeningKeywordRepository jobOpeningKeywordRepository;
    private final UserKeywordRepository userKeywordRepository;
    private final UserFindByService userFindByService;

    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void findJobOpeningListIncludingUserKeyword() {

        List<User> userList = userFindByService.findAllUsers();

        for (User user : userList) {
            Long userId = user.getId();

            List<Long> keywordIdList = userKeywordRepository.findKeywordIdsByUserId(userId);
            log.info("{}번 사용자의 키워드 ID 목록: {}", userId, keywordIdList);

            List<Long> jobOpeningIdList = jobOpeningKeywordRepository.findJobOpeningIdListByKeywordId(keywordIdList);
            log.info("{}번 사용자가 받을 채용 공고 ID 목록: {}", userId, jobOpeningIdList);

            List<JobOpening> jobOpeningList = jobOpeningRepository.findAllById(jobOpeningIdList);
            log.info("채용 공고 수: {}", jobOpeningList.size());

            jobOpeningList.forEach(
                jobOpening -> {
                    log.info("{}번 사용자가 알림 받을 채용 공고 URL: {}", userId, jobOpening.getJobOpeningUrl());
                    log.info("{}번 사용자가 알림 받을 채용 공고 제목: {}", userId, jobOpening.getTitle());
                }
            );
        }
    }
}