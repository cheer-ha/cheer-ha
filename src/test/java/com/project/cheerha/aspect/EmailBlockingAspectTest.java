package com.project.cheerha.aspect;

import com.project.cheerha.common.aop.block.EmailBlockingAspect;
import com.project.cheerha.common.exception.auth.AuthErrorCode;
import com.project.cheerha.common.exception.auth.UnAuthorizedException;
import com.project.cheerha.common.repository.KeyValueCommandRepository;
import com.project.cheerha.domain.auth.dto.request.CreateLoginRequestDto;
import com.project.cheerha.domain.auth.entity.BannedEmail;
import com.project.cheerha.domain.auth.repository.BannedEmailRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailBlockingAspectTest {

    @InjectMocks
    private EmailBlockingAspect emailBlockingAspect;

    @Mock
    private KeyValueCommandRepository kValueCommandRepository;

    @Mock
    private BannedEmailRepository bannedEmailRepository;

    @Mock
    private ProceedingJoinPoint joinPoint;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String FAIL_COUNT_KEY = "fail:email:" + TEST_EMAIL;

    @Test
    void 로그인_성공시_실패카운트_삭제됨() throws Throwable {
        Object[] args = new Object[]{new CreateLoginRequestDto(TEST_EMAIL, "password")};

        when(bannedEmailRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.proceed(args)).thenReturn("로그인 성공");

        Object result = emailBlockingAspect.blockAbnormalEmail(joinPoint);

        assertEquals("로그인 성공", result);
        verify(kValueCommandRepository, times(1)).removeValue(FAIL_COUNT_KEY);
    }


    @Test
    void 로그인_실패시_실패카운트_증가() throws Throwable {
        Object[] args = new Object[]{new CreateLoginRequestDto(TEST_EMAIL, "wrongPassword")};

        when(bannedEmailRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.proceed(any(Object[].class))).thenThrow(new UnAuthorizedException(AuthErrorCode.INVALID_PASSWORD));

        when(kValueCommandRepository.incrementValue(FAIL_COUNT_KEY)).thenReturn(1L);

        Exception exception = assertThrows(Exception.class, () -> emailBlockingAspect.blockAbnormalEmail(joinPoint));
        assertEquals("패스워드가 잘못되었습니다.", exception.getMessage());

        verify(kValueCommandRepository, times(1)).incrementValue(FAIL_COUNT_KEY);
        verify(kValueCommandRepository, times(1)).expireValue(FAIL_COUNT_KEY, 3, TimeUnit.DAYS);
    }

    @Test
    void 로그인_5회실패_이메일차단() throws Throwable {
        Object[] args = new Object[]{new CreateLoginRequestDto(TEST_EMAIL, "wrongPassword")};

        when(bannedEmailRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.proceed(any(Object[].class))).thenThrow(new UnAuthorizedException(AuthErrorCode.INVALID_PASSWORD));

        when(kValueCommandRepository.incrementValue(FAIL_COUNT_KEY)).thenReturn(5L);

        Exception exception = assertThrows(Exception.class, () -> emailBlockingAspect.blockAbnormalEmail(joinPoint));
        assertEquals("패스워드가 잘못되었습니다.", exception.getMessage());

        verify(bannedEmailRepository, times(1)).save(any(BannedEmail.class));
        verify(kValueCommandRepository, times(1)).removeValue(FAIL_COUNT_KEY);
    }

    @Test
    void 차단된_이메일_로그인시_예외발생() throws Throwable {
        when(bannedEmailRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);
        when(joinPoint.getArgs()).thenReturn(new Object[]{new CreateLoginRequestDto(TEST_EMAIL, "패스워드가 잘못되었습니다.")});

        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class,
                () -> emailBlockingAspect.blockAbnormalEmail(joinPoint));

        assertEquals("로그인이 임시차단된 이메일입니다. 비밀번호를 리셋해주세요", exception.getMessage());

        verify(joinPoint, never()).proceed();
    }
}
