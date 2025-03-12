package com.project.cheerha.common.aop;

import com.project.cheerha.domain.jobopening.service.ViewCountManager;
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

    private final ViewCountManager viewCountManager;

    @AfterReturning("execution(* com.project.cheerha.domain.jobopening.controller.JobOpeningController.getRedirectedView(..))")
    public void increaseViewCount(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];

        viewCountManager.increaseViewCount(id);
    }
}
