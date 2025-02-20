package com.project.cheerha.common.block;

import com.project.cheerha.common.exception.auth.AuthErrorCode;
import com.project.cheerha.common.exception.auth.UnAuthorizedException;
import com.project.cheerha.domain.auth.dto.request.CreateLoginRequestDto;
import com.project.cheerha.domain.auth.entity.BannedEmail;
import com.project.cheerha.domain.auth.repository.BannedEmailRepository;
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
    private final BannedEmailRepository bannedEmailRepository;

    private static final String FAIL_PREFIX = "fail:email:";
    private static final long EMAIL_FAIL_DURATION = 3;    //로그인 실패 시 실패데이터 3일간 유지
    private static final int MAX_FAILED_COUNT = 5;  //5회 실패 시 차단
    private static final String BAN_MASSAGE = "비밀번호 입력 5회 실패";    //db에 저장되고, 로그에 출력되는 메세지

    /**
     * 같은 이메일로 5회 이상 로그인 실패한 경우 밴하는 메서드입니다.
     * TODO: 이후 이메일 인증을 통해 푸는 방법도 고려해볼만합니다
     */
    @Around("execution(* com.project.cheerha.domain.auth.controller.AuthController.login(..))")
    public Object blockAbnormalEmail(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        CreateLoginRequestDto dto = (CreateLoginRequestDto) args[0];
        String email = dto.email();
        String failCountKey = FAIL_PREFIX + email;

        //차단된 이메일인지 확인
        if (bannedEmailRepository.existsByEmail(email)) {
            log.warn("임시차단된 사용자의 로그인 요청: {}", email);
            throw new UnAuthorizedException(AuthErrorCode.BANNED_EMAIL);
        }

        try {
            Object result = joinPoint.proceed(args);
            //로그인 성공 시 failCount 삭제
            redisTemplate.delete(failCountKey);
            return result;
        } catch (Exception e) {
            if(Objects.equals(e.getMessage(), "패스워드가 잘못되었습니다.")){
                log.error("로그인 실패: {}", email);

                //잘못된 비밀번호 입력 시 count 1회 추가, 첫 추가 시 ttl 설정
                long failedAttempts = redisTemplate.opsForValue().increment(failCountKey);
                if (failedAttempts == 1) {
                    redisTemplate.expire(failCountKey, EMAIL_FAIL_DURATION, TimeUnit.DAYS);
                }

                //잘못된 시도 5회 시 이메일 차단
                if (failedAttempts >= MAX_FAILED_COUNT) {
                    String message = BAN_MASSAGE;
                    BannedEmail bannedEmail = BannedEmail.of(
                            email,
                            message
                    );
                    log.warn("email {} 차단 완료 : {}", email, message);
                    bannedEmailRepository.save(bannedEmail);
                    redisTemplate.delete(failCountKey);
                }
            }
            throw e;
        }
    }
}
