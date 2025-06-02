package com.devoops.controller;

import com.devoops.domain.entity.auth.UserInfo;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.UserSaveRequest;
import com.devoops.dto.response.UserSaveResponse;
import com.devoops.dto.response.UserTokenResponse;
import com.devoops.service.auth.AuthService;
import com.devoops.service.auth.cookie.CookieManager;
import com.devoops.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private static final String REFRESH_TOKEN = "refreshToken";

    private final CookieManager cookieManager;
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/api/auth/github")
    public ResponseEntity<UserSaveResponse> issueToken(@RequestBody @Valid UserSaveRequest userSaveRequest) {
        UserInfo userInfo = authService.getUserInfo(userSaveRequest);
        User savedUser = userService.save(new User(userInfo.externalId()));
        UserTokenResponse userTokens = authService.issueToken(savedUser);
        ResponseCookie cookie = cookieManager.createCookie(REFRESH_TOKEN, userTokens.refreshToken(),
                userTokens.refreshTokenExpiration());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, userTokens.accessToken())
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new UserSaveResponse(savedUser));
    }

    @PostMapping("/api/auth/github/reissue")
    public ResponseEntity<?> reIssueToken() {
        return ResponseEntity.ok().build();
    }
}
