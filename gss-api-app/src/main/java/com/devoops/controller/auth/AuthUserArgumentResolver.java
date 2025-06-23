package com.devoops.controller.auth;

import com.devoops.service.auth.AuthService;
import com.devoops.service.auth.jwt.TokenType;
import com.devoops.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;
    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUser.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        String accessToken = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(accessToken)) {
            throw new RuntimeException("401 인증 에러"); //TODO 추후 커스텀 에러로 수정
        }
        String providerId = authService.resolveToken(accessToken, TokenType.ACCESS_TOKEN);
        return userService.findByProviderId(Long.parseLong(providerId));
    }
}
