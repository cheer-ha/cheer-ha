package com.project.cheerha.common.block;

import com.project.cheerha.common.exception.auth.AuthErrorCode;
import com.project.cheerha.common.exception.auth.UnAuthorizedException;
import com.project.cheerha.domain.auth.dto.request.CreateLoginRequestDto;
import com.project.cheerha.domain.auth.entity.BannedIp;
import com.project.cheerha.domain.auth.repository.BannedIpRepository;
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
    private final BannedIpRepository bannedIpRepository;

    private static final String LOGIN_ATTEMPT_PREFIX = "attempt:ip:";
    private static final long ATTEMPT_TTL = 15;        //15분 동안 시도 기록 유지
    private static final int MAX_DIFFERENT_EMAILS = 3; //서로 다른 이메일 4개 이상 감지되면 차단(3개까지 허용)
    private static final String BAN_MASSAGE = "서로 다른 4개의 이메일 감지";

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

        String ip = getClientIp(request);
        String redisAttemptKey = LOGIN_ATTEMPT_PREFIX + ip;

        //차단된 ip 인지 확인
        if (bannedIpRepository.existsByIp(ip)) {
            log.warn("차단된 IP 로그인 시도: {}", ip);
            throw new UnAuthorizedException(AuthErrorCode.BANNED_IP);
        }

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
                String message = BAN_MASSAGE;
                BannedIp bannedIp = BannedIp.of(
                    ip, message
                );
                bannedIpRepository.save(bannedIp);
                log.warn("ip {} 차단 완료 : {}", ip, message);
                redisTemplate.delete(redisAttemptKey);
            }
            throw e;
        }
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * 사용자의 ip 를 추출합니다.
     * @return ipV6이 있다면 변별력을 위해 V6 을 우선적으로 가져옵니다. v6이 없다면 v4를 가져옵니다.
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            ip = ip.split(",")[0].trim();
        } else {
            ip = request.getRemoteAddr();
        }

        if (ip.contains(":")) {
            log.info("IPv6 추출 성공: {}", ip);
        } else {
            log.info("IPv4 추출 성공: {}", ip);
        }
        return ip;
    }

}
