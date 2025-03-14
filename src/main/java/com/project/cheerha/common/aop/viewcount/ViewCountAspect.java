package com.project.cheerha.common.aop.viewcount;

import com.project.cheerha.common.redis.viewcount.RedisViewCountManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ViewCountAspect {

    private final RedisViewCountManager redisViewCountManager;

    @AfterReturning("execution(* com.project.cheerha.domain.jobopening.controller.JobOpeningController.getRedirectedView(..))")
    public void increaseViewCount(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];

        redisViewCountManager.increaseViewCount(id);
    }
}
