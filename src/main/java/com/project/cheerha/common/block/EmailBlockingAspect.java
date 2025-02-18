package com.project.cheerha.common.block;

import com.project.cheerha.common.exception.auth.AuthErrorCode;
import com.project.cheerha.common.exception.auth.UnAuthorizedException;
import com.project.cheerha.domain.auth.dto.request.CreateLoginRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class EmailBlockingAspect {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String BLOCK_PREFIX = "block:";
    private static final String FAIL_PREFIX = "fail:";
    private static final long EMAIL_BLOCK_DURATION = 15;  //15분 동안 차단
    private static final long EMAIL_FAIL_DURATION = 3;    //로그인 실패 시 실패데이터 3일간 유지
    private static final int MAX_FAILED_COUNT = 5;  //5회 실패 시 차단

    /**
     * 같은 이메일로 5회 이상 로그인 실패한 경우 15분간 차단하는 메서드입니다
     * 차단된 상태로 로그인 재시도 시 다시 15분 차단당합니다
     * TODO: 이후 이메일 인증을 통해 푸는 방법도 고려해볼만합니다
     */
    @Around("execution(* com.project.cheerha.domain.auth.controller.AuthController.login(..))")
    public Object blockEmailWhenLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        CreateLoginRequestDto dto = (CreateLoginRequestDto) args[0];
        String email = dto.email();
        String redisKey = BLOCK_PREFIX + email;
        String failCountKey = FAIL_PREFIX + email;

        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            log.warn("임시차단된 사용자의 로그인 요청: {}", email);
            redisTemplate.opsForValue().set(redisKey, "blocked", EMAIL_BLOCK_DURATION, TimeUnit.MINUTES);
            throw new UnAuthorizedException(AuthErrorCode.BLOCKED_EMAIL);
        }

        try {
            Object result = joinPoint.proceed(args);
            redisTemplate.delete(failCountKey);
            return result;
        } catch (Exception e) {
            if(Objects.equals(e.getMessage(), "패스워드가 잘못되었습니다.")){
                log.error("로그인 실패: {}", email);

                long failedAttempts = redisTemplate.opsForValue().increment(failCountKey);
                if (failedAttempts == 1) {
                    redisTemplate.expire(failCountKey, EMAIL_FAIL_DURATION, TimeUnit.DAYS);
                }

                if (failedAttempts >= MAX_FAILED_COUNT) {
                    redisTemplate.opsForValue().set(redisKey, "blocked", EMAIL_BLOCK_DURATION, TimeUnit.MINUTES);
                    log.warn("임시차단된 이메일: {} 이 {} 분 간 차단되었습니다", email, EMAIL_BLOCK_DURATION);
                    redisTemplate.expire(failCountKey, EMAIL_FAIL_DURATION, TimeUnit.MINUTES);
                }
            }
            throw e;
        }
    }
}
