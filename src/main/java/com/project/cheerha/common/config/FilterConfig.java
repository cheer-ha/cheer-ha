package com.project.cheerha.common.config;

import com.project.cheerha.common.exception.handler.FilterExceptionHandler;
import com.project.cheerha.common.filter.IpBlockingFilter;
import com.project.cheerha.common.filter.JwtFilter;
import com.project.cheerha.common.properties.JwtSecurityProperties;
import com.project.cheerha.domain.auth.service.BlackListService;
import com.project.cheerha.common.util.JwtUtil;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtUtil jwtUtil;
    private final JwtSecurityProperties jwtSecurityProperties;
    private final BlackListService blackListService;
    private final FilterExceptionHandler filterExceptionHandler;

    @Bean
    public FilterRegistrationBean<Filter> ipBlockingFilterRegistration(IpBlockingFilter ipBlockingFilter) {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(ipBlockingFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter(jwtSecurityProperties, blackListService, jwtUtil, filterExceptionHandler));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
