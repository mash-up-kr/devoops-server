package com.devoops.service.facade;

import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.UserSaveRequest;
import com.devoops.dto.response.AuthResponse;
import com.devoops.dto.response.UserSaveResponse;
import com.devoops.dto.response.UserTokenRefreshResponse;
import com.devoops.dto.response.UserTokenResponse;
import com.devoops.service.auth.AuthService;
import com.devoops.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacadeService {

    private final UserService userService;
    private final AuthService authService;

    public UserSaveResponse issueToken(UserSaveRequest saveRequest) {
        AuthResponse authResponse = authService.getUserInfo(saveRequest.githubAccessToken());
        GithubToken accessToken = new GithubToken(authResponse.githubToken());
        User user = new User(authResponse.providerId(), authResponse.nickname(), authResponse.profileImageUrl(),
                accessToken);
        User savedUser = userService.save(user);
        UserTokenResponse userTokens = authService.issueToken(savedUser.getId());
        return new UserSaveResponse(savedUser, userTokens.accessToken(), userTokens.refreshToken());
    }

    public UserTokenRefreshResponse refresh(String refreshToken) {
        UserTokenResponse userTokenResponse = authService.reissueToken(refreshToken);
        return new UserTokenRefreshResponse(userTokenResponse.accessToken(), userTokenResponse.refreshToken());
    }

    public UserTokenRefreshResponse refreshV1(String accessToken, String refreshToken) {
        UserTokenResponse userTokenResponse = authService.refreshTokenV1(accessToken, refreshToken);
        return new UserTokenRefreshResponse(userTokenResponse.accessToken(), userTokenResponse.refreshToken());
    }

    public void logout(String refreshToken, User user) {
        authService.logout(refreshToken, user);
    }

    public void logoutV1(String accessToken, String refreshToken, User user) {
        authService.logoutV1(accessToken, refreshToken, user);
    }
}
