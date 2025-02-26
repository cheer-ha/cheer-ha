package com.project.cheerha.domain.user.service;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CheckDailyEmailCount {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String DAILY_EMAIL_COUNT_PREFIX = "daily_email_count";

    /**
     * 하루 발송 제한을 체크하고 발송 카운트를 1 증가시키는 메서드(자정에 초기화됨)
     * @param email 이메일
     * @param operationKey 어떤 목적의 이메일인지 구분하는 키
     * @param dailyLimit 하루 발송 제한 횟수
     */
    public void checkAndIncrementDailyLimit(String email, String operationKey, int dailyLimit) {
        String today = LocalDate.now().toString();
        String dailyCountKey = DAILY_EMAIL_COUNT_PREFIX + ":" + operationKey + ":" + email + ":" + today;

        int requestCount = Optional.ofNullable(redisTemplate.opsForValue().get(dailyCountKey))
                .map(Integer::valueOf)
                .orElse(0);

        if (requestCount >= dailyLimit) {
            throw new BadRequestException(ClientErrorCode.EXCEEDED_DAILY_LIMIT);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        long secondsToMidnight = Duration.between(now, midnight).getSeconds();

        redisTemplate.opsForValue().increment(dailyCountKey);

        if (requestCount == 0) {
            redisTemplate.expire(dailyCountKey, secondsToMidnight, TimeUnit.SECONDS);
        }
    }

}
