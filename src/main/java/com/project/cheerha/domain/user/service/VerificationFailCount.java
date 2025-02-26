package com.project.cheerha.domain.user.service;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class VerificationFailCount {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String VERIFICATION_FAIL_COUNT_PREFIX = "verification_fail_count";
    private static final long FAIL_COUNT_EXPIRE_MINUTES = 5;
    private static final int MAX_FAIL_COUNT = 3;

    /**
     * 인증 실패 시 카운트를 1 증가시키고
     * 최대 횟수를 초과하면 예외 발생
     */
    public void incrementFailCount(String email, String operationKey) {
        String failCountKey = VERIFICATION_FAIL_COUNT_PREFIX + ":" + operationKey + ":" + email;

        int failCount = Optional.ofNullable(redisTemplate.opsForValue().get(failCountKey))
                .map(Integer::valueOf)
                .orElse(0);
        failCount++;

        if (failCount >= MAX_FAIL_COUNT) {
            throw new BadRequestException(ClientErrorCode.MAX_FAIL_COUNT_EXCEEDED);
        }

        redisTemplate.opsForValue().set(failCountKey,
                String.valueOf(failCount),
                FAIL_COUNT_EXPIRE_MINUTES,
                TimeUnit.MINUTES);
    }
}
