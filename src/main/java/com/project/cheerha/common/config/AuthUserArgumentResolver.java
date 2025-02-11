package com.project.cheerha.common.config;

import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.user.entity.User.Role;
import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 컨트롤러의 파라미터에 `AuthUser` 타입이 있으면 자동으로 값을 주입하는 Argument Resolver.
 */
@Component // Spring Bean으로 등록됨
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 컨트롤러 메서드에서 `AuthUser` 타입이 있으면 지원.
     */
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
        Object emailObj = request.getAttribute("email");
        Object roleObj = request.getAttribute("userRole");

        //사용자 정보 없으면 null 반환
        if (userIdObj == null || emailObj == null || roleObj == null) {
            return null;
        }

        //string 으로 변환
        Long userId = Long.parseLong(userIdObj.toString());
        String email = emailObj.toString();
        Role userRole = Role.valueOf(roleObj.toString());

        return new AuthUser(userId, email, userRole);
    }
}
