package com.project.cheerha.jwt;

import com.project.cheerha.common.config.JwtFilter;
import com.project.cheerha.common.exception.handler.FilterExceptionHandler;
import com.project.cheerha.common.properties.JwtSecurityProperties;
import com.project.cheerha.common.redis.RedisBlackListService;
import com.project.cheerha.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoFilterTest {

    @Mock
    private RedisBlackListService redisBlackListService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private FilterExceptionHandler filterExceptionHandler;

    @InjectMocks
    private JwtFilter jwtFilter;

    @Test
    void testFilter_화이트리스트일때() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/auth/signup");

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testFilter_토큰값이_비어있을때() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        request.setRequestURI("/api/protected");
        request.addHeader("Authorization", "");

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterExceptionHandler, times(1))
                .sendErrorResponse(any(HttpServletResponse.class), eq(HttpStatus.BAD_REQUEST), anyString());
    }



    @Test
    void testFilter_토큰값이_정상일때() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/protected");
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtil.substringToken("Bearer validToken")).thenReturn("validToken");
        when(redisBlackListService.isBlackList("validToken")).thenReturn(false);

        Claims claims = mock(Claims.class);
        when(jwtUtil.extractClaims("validToken")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("1");
        when(claims.get("userRole", String.class)).thenReturn("USER");

        JwtSecurityProperties securityProperties = mock(JwtSecurityProperties.class);
        JwtSecurityProperties.Token token = mock(JwtSecurityProperties.Token.class);

        when(securityProperties.getToken()).thenReturn(token);
        when(token.getPrefix()).thenReturn("Bearer ");

        jwtFilter = new JwtFilter(securityProperties, redisBlackListService, jwtUtil, filterExceptionHandler);
        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testFilter_만료된_토큰일때() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/protected");
        when(request.getHeader("Authorization")).thenReturn("Bearer expiredToken");
        when(jwtUtil.substringToken("Bearer expiredToken")).thenReturn("expiredToken");
        when(jwtUtil.extractClaims("expiredToken")).thenThrow(ExpiredJwtException.class);

        JwtSecurityProperties securityProperties = mock(JwtSecurityProperties.class);

        jwtFilter = new JwtFilter(securityProperties, redisBlackListService, jwtUtil, filterExceptionHandler);
        jwtFilter.doFilter(request, response, filterChain);

        verify(filterExceptionHandler, times(1))
                .sendErrorResponse(any(HttpServletResponse.class), eq(HttpStatus.UNAUTHORIZED), anyString());
    }

    @Test
    void testFilter_블랙리스트_토큰일때() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/protected");
        when(request.getHeader("Authorization")).thenReturn("Bearer blackListToken");
        when(jwtUtil.substringToken("Bearer blackListToken")).thenReturn("blackListToken");

        when(redisBlackListService.isBlackList("blackListToken")).thenReturn(true);

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterExceptionHandler, times(1))
                .sendErrorResponse(any(HttpServletResponse.class), eq(HttpStatus.UNAUTHORIZED), anyString());
    }

    @Test
    void testFilter_Authorization_헤더가_없을때() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        request.setRequestURI("/api/protected");

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterExceptionHandler, times(1))
                .sendErrorResponse(any(HttpServletResponse.class), eq(HttpStatus.BAD_REQUEST), anyString());
    }

    @Test
    void testFilter_유저정보가_없는_토큰일때() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/protected");
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtil.substringToken("Bearer validToken")).thenReturn("validToken");
        when(redisBlackListService.isBlackList("validToken")).thenReturn(false);

        Claims claims = mock(Claims.class);
        when(jwtUtil.extractClaims("validToken")).thenReturn(claims);

        when(claims.getSubject()).thenReturn(null);

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterExceptionHandler, times(1))
                .sendErrorResponse(any(HttpServletResponse.class), eq(HttpStatus.BAD_REQUEST), anyString());
    }

    @Test
    void testFilter_유효하지_않은_권한일때() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/protected");
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtil.substringToken("Bearer validToken")).thenReturn("validToken");
        when(redisBlackListService.isBlackList("validToken")).thenReturn(false);

        Claims claims = mock(Claims.class);
        when(jwtUtil.extractClaims("validToken")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("1");

        when(claims.get("userRole", String.class)).thenReturn("INVALID_ROLE");

        JwtSecurityProperties securityProperties = mock(JwtSecurityProperties.class);
        JwtSecurityProperties.Token token = mock(JwtSecurityProperties.Token.class);

        when(securityProperties.getToken()).thenReturn(token);
        when(token.getPrefix()).thenReturn("Bearer ");

        jwtFilter = new JwtFilter(securityProperties, redisBlackListService, jwtUtil, filterExceptionHandler);
        jwtFilter.doFilter(request, response, filterChain);

        verify(filterExceptionHandler, times(1))
                .sendErrorResponse(any(HttpServletResponse.class), eq(HttpStatus.BAD_REQUEST), anyString());
    }

}
