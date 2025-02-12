package com.project.cheerha.common.config;

import com.project.cheerha.common.properties.JwtSecurityProperties;
import com.project.cheerha.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtUtil jwtUtil;
    private final JwtSecurityProperties jwtSecurityProperties;

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter(jwtSecurityProperties, jwtUtil));
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
