package com.devoops.dto;

import lombok.Getter;

@Getter
public class GithubPRUrl {

    private static final String GITHUB_REPO_API_URL = "https://api.github.com/repos";
    private static final String GITHUB_REPO_URL = "https://github.com";

    private final String value;

    public GithubPRUrl(String value) {
        this.value = parsed(value);
    }

    private String parsed(String url) {
        if(url.startsWith(GITHUB_REPO_API_URL)) {
            return url.replace(GITHUB_REPO_API_URL, GITHUB_REPO_URL);
        }
        return url;
    }
}
