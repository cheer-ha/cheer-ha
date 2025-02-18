package com.project.cheerha.common.logging;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LockRetryLoggingAspect {

    private int retryCount;

    /**
     * 채용공고 URL 리다이렉트 메서드(getJobOpeningUrlAndIncreaseViewCount)를 실행할 때
     * - 메서드가 실행되기 전에 id 값을 로그로 출력
     * - 메서드 실행 후 리턴된 URL을 로그로 출력
     * - 예외 발생 시 로그로 출력
     */
    @Around("execution(* com.project.cheerha.domain.jobOpening.service.JobOpeningService.getJobOpeningUrlAndIncreaseViewCount(..))")
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

    /**
     * 낙관적 락 재시도 메서드(increaseViewCountRetry)가 실행되기 직전에
     * retryCount 변수를 만들어 증가시키는 방법을 사용하여 재시도 횟수를 로그로 출력
     */
    @Before("execution(* com.project.cheerha.domain.jobOpening.service.JobOpeningService.increaseViewCountRetry(..))")
    public void beforeRetry() {
        retryCount++;
        log.warn("낙관적 락 재시도 횟수 : {}", retryCount);
    }

    /**
     * increaseViewCountRetry 메서드가 실행되다가 예외가 발생했을 때
     * - 예외가 발생하면 로그를 출력
     */
    @AfterThrowing(value = "execution(* com.project.cheerha.domain.jobOpening.service.JobOpeningService.increaseViewCountRetry(..))", throwing = "ex")
    public void afterRetryFailure(Exception ex) {
        log.error("최대 재시도 횟수 초과: {}", ex.getMessage());
    }

}
