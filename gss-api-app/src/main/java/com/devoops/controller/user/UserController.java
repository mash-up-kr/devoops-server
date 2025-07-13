package com.devoops.controller.user;

import com.devoops.controller.auth.AuthUser;
import com.devoops.controller.docs.UserControllerSwagger;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.response.UserReadResponse;
import com.devoops.service.facade.UserFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerSwagger {

    private final UserFacadeService userFacadeService;

    @Override
    @GetMapping("/me")
    public ResponseEntity<UserReadResponse> getMyInfo(@AuthUser User user) {
        UserReadResponse response = userFacadeService.getUser(user.getId());
        return ResponseEntity.ok(response);
    }
}
