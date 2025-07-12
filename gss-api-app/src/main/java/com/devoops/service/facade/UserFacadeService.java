package com.devoops.service.facade;

import com.devoops.domain.entity.user.User;
import com.devoops.dto.response.UserReadResponse;
import com.devoops.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacadeService {

    private final UserService userService;

    public UserReadResponse getUser(long userId) {
        User user = userService.findById(userId);
        return new UserReadResponse(user);
    }
}
