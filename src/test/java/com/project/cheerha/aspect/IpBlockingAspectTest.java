package com.project.cheerha.aspect;

import com.project.cheerha.common.aop.block.IpBlockingAspect;
import com.project.cheerha.common.exception.auth.AuthErrorCode;
import com.project.cheerha.common.exception.auth.UnAuthorizedException;
import com.project.cheerha.common.util.IpUtil;
import com.project.cheerha.domain.auth.dto.request.CreateLoginRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IpBlockingAspectTest {

    @InjectMocks
    private IpBlockingAspect ipBlockingAspect;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private ListOperations<String, String> listOperations;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private HttpServletRequest request;

    private static final String TEST_IP = "192.168.1.1";
    private static final String BLOCK_KEY = "block:ip:" + TEST_IP;
    private static final String ATTEMPT_KEY = "attempt:ip:" + TEST_IP;
    private static final String EMAIL_1 = "email1@example.com";
    private static final String EMAIL_2 = "email2@example.com";
    private static final String EMAIL_3 = "email3@example.com";
    private static final String EMAIL_4 = "email4@example.com";

    @BeforeEach
    void setUp() {
        ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);
        when(attributes.getRequest()).thenReturn(request);
        RequestContextHolder.setRequestAttributes(attributes);
        when(IpUtil.getClientIp(request)).thenReturn(TEST_IP);
    }

    @Test
    void 로그인_성공시_차단되지않음() throws Throwable {
        Object[] args = new Object[]{new CreateLoginRequestDto(EMAIL_1, "password")};
        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.proceed()).thenReturn("로그인 성공");

        Object result = ipBlockingAspect.blockAbnormalIp(joinPoint);

        assertEquals("로그인 성공", result);
        verify(redisTemplate, never()).opsForValue();
        verify(redisTemplate, never()).opsForList();
    }

    @Test
    void 로그인_실패시_레디스저장() throws Throwable {
        Object[] args = new Object[]{new CreateLoginRequestDto(EMAIL_3, "wrongPassword")};
        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.proceed()).thenThrow(new UnAuthorizedException(AuthErrorCode.INVALID_PASSWORD));

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.range(ATTEMPT_KEY, 0, -1)).thenReturn(List.of(EMAIL_1, EMAIL_2));

        Exception exception = assertThrows(RuntimeException.class,
                () -> ipBlockingAspect.blockAbnormalIp(joinPoint));

        assertEquals("패스워드가 잘못되었습니다.", exception.getMessage());

        verify(listOperations, times(1)).rightPush(ATTEMPT_KEY, EMAIL_3);
        verify(redisTemplate, times(1)).expire(ATTEMPT_KEY, 15, TimeUnit.MINUTES);
    }

    @Test
    void 서로다른_이메일4개_차단() throws Throwable {
        Object[] args = new Object[]{new CreateLoginRequestDto(EMAIL_4, "wrongPassword")};
        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.proceed()).thenThrow(new UnAuthorizedException(AuthErrorCode.INVALID_PASSWORD));

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(listOperations.range(ATTEMPT_KEY, 0, -1)).thenReturn(List.of(EMAIL_1, EMAIL_2, EMAIL_3));

        Exception exception = assertThrows(RuntimeException.class,
                () -> ipBlockingAspect.blockAbnormalIp(joinPoint));

        assertEquals("패스워드가 잘못되었습니다.", exception.getMessage());

        verify(valueOperations, times(1)).set(BLOCK_KEY, "blocked", 30, TimeUnit.SECONDS);
        verify(redisTemplate, times(1)).delete(ATTEMPT_KEY);
    }
}
