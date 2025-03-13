package com.project.cheerha.domain.user.service;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.common.repository.KeyValueCommandRepository;
import com.project.cheerha.common.repository.KeyValueQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CheckDailyEmailCount {

    private final KeyValueCommandRepository keyValueCommandRepository;
    private final KeyValueQueryRepository keyValueQueryRepository;

    private static final String DAILY_EMAIL_COUNT_PREFIX = "daily_email_count";
    private static final int MAX_SENT_COUNT = 3;

    /**
     * 하루 발송 제한을 체크하고 발송 카운트를 1 증가시키는 메서드(자정에 초기화됨)
     * @param email 이메일
     * @param operationKey 어떤 목적의 이메일인지 구분하는 키
     */
    public void incrementDailyLimit(String email, String operationKey) {
        String today = LocalDate.now().toString();
        String dailyCountKey = DAILY_EMAIL_COUNT_PREFIX + ":" + operationKey + ":" + email + ":" + today;

        int requestCount = Optional.ofNullable(keyValueQueryRepository.getValue(dailyCountKey))
                .map(Integer::valueOf)
                .orElse(0);

        if (requestCount >= MAX_SENT_COUNT) {
            throw new BadRequestException(ClientErrorCode.DAILY_LIMIT_EXCEEDED);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        long secondsToMidnight = Duration.between(now, midnight).getSeconds();

        keyValueCommandRepository.incrementValue(dailyCountKey);

        if (requestCount == 0) {
            keyValueCommandRepository.expireValue(dailyCountKey, secondsToMidnight, TimeUnit.SECONDS);
        }
    }
}
