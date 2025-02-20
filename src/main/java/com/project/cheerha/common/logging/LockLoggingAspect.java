package com.project.cheerha.common.logging;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LockLoggingAspect {

    /**
     * 채용공고 URL 리다이렉트 메서드(getJobOpeningUrlAndIncreaseViewCount)를 실행할 때
     * - 메서드가 실행되기 전에 id 값을 로그로 출력
     * - 메서드 실행 후 리턴된 URL을 로그로 출력
     * - 예외 발생 시 로그로 출력
     */
    @Around("execution(* com.project.cheerha.domain.jobopening.service.JobOpeningService.getJobOpeningUrlAndIncreaseViewCount(..))")
    public Object redirectUrlLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs(); //메서드의 매개변수를 가져옴, 즉 id값 가져오기
        log.info("채용공고 id: {}", Arrays.toString(args));

        Object result;
        try {
            result = joinPoint.proceed(); // 실제 메서드 실행
            log.info("최종 리다이렉트 URL: {}", result);
        } catch (Exception e) {
            log.error("예외발생: {}", e.getMessage());
            throw e;
        }
        return result;
    }

}
