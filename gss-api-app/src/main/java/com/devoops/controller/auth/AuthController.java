package com.devoops.controller.auth;

import com.devoops.controller.docs.AuthControllerSwagger;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.LogoutV1Request;
import com.devoops.dto.request.RefreshTokenV1Request;
import com.devoops.dto.request.UserSaveRequest;
import com.devoops.dto.response.UserSaveResponse;
import com.devoops.dto.response.UserTokenRefreshResponse;
import com.devoops.service.facade.AuthFacadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerSwagger {

    private static final String REFRESH_TOKEN = "refreshToken";

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
        UserTokenRefreshResponse response = authFacadeService.refresh(token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    @PostMapping("/api/v1/auth/github/refresh")
    public ResponseEntity<UserTokenRefreshResponse> reIssueTokenV1(
            @RequestBody RefreshTokenV1Request refreshTokenV1Request
    ) {
        UserTokenRefreshResponse response = authFacadeService.refreshV1(
                refreshTokenV1Request.accessToken(),
                refreshTokenV1Request.refreshToken()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    @PostMapping("/api/auth/logout")
    public ResponseEntity<Void> logout(
            @AuthUser User user,
            @CookieValue(REFRESH_TOKEN) String token
    ) {
        authFacadeService.logout(token, user);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<Void> logoutV1(
            @AuthUser User user,
            @RequestBody @Valid LogoutV1Request request
    ) {
        authFacadeService.logoutV1(request.accessToken(), request.refreshToken(), user);
        return ResponseEntity.ok().build();
    }
}
