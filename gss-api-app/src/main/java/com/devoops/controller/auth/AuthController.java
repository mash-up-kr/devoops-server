package com.devoops.controller.auth;

import com.devoops.controller.docs.AuthControllerSwagger;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.UserSaveRequest;
import com.devoops.dto.response.AuthResponse;
import com.devoops.dto.response.UserSaveResponse;
import com.devoops.dto.response.UserTokenResponse;
import com.devoops.service.auth.AuthService;
import com.devoops.service.auth.cookie.CookieManager;
import com.devoops.service.auth.jwt.JwtToken;
import com.devoops.service.auth.jwt.TokenType;
import com.devoops.service.user.UserService;
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
    private final AuthService authService;
    private final UserService userService;

    @Override
    @PostMapping("/api/auth/github")
    public ResponseEntity<UserSaveResponse> issueToken(@RequestBody @Valid UserSaveRequest userSaveRequest) {
        AuthResponse authResponse = authService.getUserInfo(userSaveRequest);
        User savedUser = userService.save(new User(authResponse.email(), authResponse.githubToken()));
        UserTokenResponse userTokens = authService.issueToken(savedUser.getEmail());
        ResponseCookie cookie = cookieManager.createCookie(REFRESH_TOKEN, userTokens.refreshToken(),
                userTokens.refreshTokenExpiration());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, userTokens.accessToken())
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new UserSaveResponse(savedUser));
    }

    @Override
    @PostMapping("/api/auth/github/reissue")
    public ResponseEntity<Void> reIssueToken(@CookieValue(REFRESH_TOKEN) String token) {
        JwtToken refreshToken = new JwtToken(token, TokenType.REFRESH_TOKEN);
        UserTokenResponse userTokens = authService.reissueToken(refreshToken);
        ResponseCookie cookie = cookieManager.createCookie(REFRESH_TOKEN, userTokens.refreshToken(),
                userTokens.refreshTokenExpiration());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, userTokens.accessToken())
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<Void> logout(
            @AuthUser User user,
            @CookieValue(REFRESH_TOKEN) String token
    ) {
        String email = authService.resolveToken(token, TokenType.REFRESH_TOKEN);
        authService.logout(user, email);
        ResponseCookie expiredRefreshTokenCookie = cookieManager.createExpiredCookie(REFRESH_TOKEN);
        System.out.println(expiredRefreshTokenCookie.toString());

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, expiredRefreshTokenCookie.toString())
                .build();
    }
}
