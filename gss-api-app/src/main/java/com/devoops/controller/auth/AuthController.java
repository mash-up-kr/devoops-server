package com.devoops.controller.auth;

import com.devoops.controller.docs.AuthControllerSwagger;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.UserSaveRequest;
import com.devoops.dto.response.UserSaveResponse;
import com.devoops.dto.response.UserTokenRefreshResponse;
import com.devoops.service.auth.cookie.CookieManager;
import com.devoops.service.auth.jwt.RefreshToken;
import com.devoops.service.facade.AuthFacadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerSwagger {

    private static final String REFRESH_TOKEN = "refreshToken";

    private final CookieManager cookieManager;
    private final AuthFacadeService authFacadeService;

    @Override
    @PostMapping("/api/auth/github")
    public ResponseEntity<UserSaveResponse> issueToken(@RequestBody @Valid UserSaveRequest userSaveRequest) {
        UserSaveResponse response = authFacadeService.issueToken(userSaveRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    @PostMapping("/api/auth/github/refresh")
    public ResponseEntity<UserTokenRefreshResponse> reIssueToken(@CookieValue(REFRESH_TOKEN) String token) {
        UserTokenRefreshResponse response = authFacadeService.refresh(new RefreshToken(token));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<Void> logout(
            @AuthUser User user,
            @CookieValue(REFRESH_TOKEN) String token
    ) {
        authFacadeService.logout(new RefreshToken(token), user);
        ResponseCookie expiredRefreshTokenCookie = cookieManager.createExpiredCookie(REFRESH_TOKEN);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, expiredRefreshTokenCookie.toString())
                .build();
    }
}
