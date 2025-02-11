package com.project.cheerha.common.config;

import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.common.exception.CustomException;
import com.project.cheerha.common.exception.ErrorCode;
import com.project.cheerha.domain.user.entity.User.Role;
import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthUser.class);
    }

    //authUser 객체 생성해 컨트롤러 메서드에 주입
    @Override
    public Object resolveArgument(@Nullable MethodParameter parameter,
        @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
        @Nullable WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        //jwt 에서 추출한 값으로 사용자 정보 가져오기
        Object userIdObj = request.getAttribute("userId");
        Object roleObj = request.getAttribute("userRole");

        //사용자 정보 없으면 에러 반환
        if (userIdObj == null || roleObj == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }

        //string 으로 변환
        Long userId = Long.parseLong(userIdObj.toString());
        Role userRole = Role.valueOf(roleObj.toString());

        return new AuthUser(userId, userRole);
    }
}
