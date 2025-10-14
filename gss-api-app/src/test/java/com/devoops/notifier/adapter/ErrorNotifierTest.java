package com.devoops.notifier.adapter;

import com.devoops.BaseControllerTest;
import com.devoops.domain.notifier.ErrorNotifier;
import com.devoops.exception.GithubNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Disabled
class ErrorNotifierTest extends BaseControllerTest {

    @Autowired
    private ErrorNotifier errorNotifier;

    @Test
    void sendMessage() {
        errorNotifier.notify(new GithubNotFoundException("Github Not Found"));
    }
}
