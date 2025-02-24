package com.project.cheerha.common.block;

import com.project.cheerha.common.util.IpUtil;
import com.project.cheerha.domain.auth.dto.request.CreateLoginRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IpBlockingAspect {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String BLOCK_PREFIX = "block:ip:";
    private static final String LOGIN_ATTEMPT_PREFIX = "attempt:ip:";
    private static final long IP_BLOCK_DURATION = 30;  //ip 30초 차단
    private static final long ATTEMPT_TTL = 15;        //15분 동안 시도 기록 유지
    private static final int MAX_DIFFERENT_EMAILS = 3; //서로 다른 이메일 4개 이상 감지되면 차단(3개까지 허용)

    /**
     * 서로 다른 이메일 4개 이상 로그인 시도 시 해당 사용자의 ip 를 차단합니다.
     * 로그인 성공 시에도 시도 기록이 남아있습니다.(로그인 후 재로그인 악용 방지)
     * TODO: 차단을 해제할 수 있는 방안도 고려해보아야 합니다.
     */
    @Around("execution(* com.project.cheerha.domain.auth.controller.AuthController.login(..))")
    public Object blockAbnormalIp(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return joinPoint.proceed();
        }

        Object[] args = joinPoint.getArgs();
        CreateLoginRequestDto dto = (CreateLoginRequestDto) args[0];
        String email = dto.email();

        String ip = IpUtil.getClientIp(request);
        String redisBlockKey = BLOCK_PREFIX + ip;
        String redisAttemptKey = LOGIN_ATTEMPT_PREFIX + ip;

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            log.error("로그인 실패: IP={}, 이메일={}", ip, email);

            //해당 ip 에서 로그인 시도한 이메일 리스트 가져오기
            List<String> attemptedEmails = redisTemplate.opsForList().range(redisAttemptKey, 0, -1);
            if (attemptedEmails == null || !attemptedEmails.contains(email)) {
                redisTemplate.opsForList().rightPush(redisAttemptKey, email);
            }

            //추가 시 ttl 설정
            redisTemplate.expire(redisAttemptKey, ATTEMPT_TTL, TimeUnit.MINUTES);

            //서로 다른 이메일이 3개 이상이면 차단
            if (!Objects.requireNonNull(attemptedEmails).contains(email) && attemptedEmails.size() >= MAX_DIFFERENT_EMAILS) {
                redisTemplate.opsForValue().set(redisBlockKey, "blocked", IP_BLOCK_DURATION, TimeUnit.SECONDS);
                log.warn("IP {} 차단됨 (서로 다른 {}개 이메일 감지됨)", ip, MAX_DIFFERENT_EMAILS + 1);
                redisTemplate.delete(redisAttemptKey);
            }
            throw e;
        }
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

}
