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

    /**
     * 컨트롤러 메서드의 파라미터가 AuthUser 와 일치하는지 확인
     * @param parameter 컨트롤러 메서드 파라미터 정보
     * @return @AuthUser 일 경우 ture 반환, 아니면 false 반환
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthUser.class);
    }

    /**
     * 요청을 보낸 사용자 정보를 JWT 에서 추출해 AuthUser 객체로 변환
     * @param parameter 현재 메서드 파라미터 정보
     * @param mavContainer 컨트롤러에서 뷰 반환할 때 사용
     * @param webRequest http 요청 정보(JWT 에서 사용자 정보를 가져오기 위함)
     * @param binderFactory 요청 데이터를 객체에 바인딩
     * @return 인증된 사용자의 ID와 역할을 포함한 AuthUser 객체
     * @throws CustomException 사용자가 인증되지 않은 경우
     */
    @Override
    public Object resolveArgument(
            @Nullable MethodParameter parameter,
            @Nullable ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Object userIdObj = request.getAttribute("userId");
        Object roleObj = request.getAttribute("userRole");

        if (userIdObj == null || roleObj == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }

        Long userId = Long.parseLong(userIdObj.toString());
        Role userRole = Role.valueOf(roleObj.toString());

        return new AuthUser(userId, userRole);
    }
}
