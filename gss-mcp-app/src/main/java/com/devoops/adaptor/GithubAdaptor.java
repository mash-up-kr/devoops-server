package com.devoops.adaptor;

import com.devoops.client.GithubWebclient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubAdaptor {

    private final GithubWebclient githubClient;

    public String getCodeChangeHistory(String diffUrl, String token) {
        return githubClient.getCodeChangeHistory(diffUrl, token);
    }

}
