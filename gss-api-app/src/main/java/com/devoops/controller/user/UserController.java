package com.devoops.controller.user;

import com.devoops.controller.auth.AuthUser;
import com.devoops.controller.docs.UserControllerSwagger;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.response.UserReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerSwagger {

    @Override
    @GetMapping
    public ResponseEntity<UserReadResponse> getUserInfo(@AuthUser User user) {
        UserReadResponse response = new UserReadResponse(user);
        return ResponseEntity.ok(response);
    }
}
