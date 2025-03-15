package com.project.cheerha.common.config;

import com.project.cheerha.common.filter.IpBlockingFilter;
import com.project.cheerha.common.filter.JwtFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> ipBlockingFilterRegistration(IpBlockingFilter ipBlockingFilter) {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(ipBlockingFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> jwtFilterRegistration(JwtFilter jwtFilter) {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
