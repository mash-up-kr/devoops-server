package com.devoops.dto.request;

import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.Getter;

@Getter
public class GithubRepoUrl {

    private static final String GITHUB_HOST_URL = "github.com";
    private static final String PATH_DELIMITER = "/";
    private static final String GIT_SUFFIX_REGEX = "\\.git$";

    private final String url;
    private final String owner;
    private final String repoName;

    public GithubRepoUrl(String url) {
        String[] pathSegments = parseUrl(url);
        this.url = url;
        this.owner = pathSegments[0];
        this.repoName = pathSegments[1].replaceAll(GIT_SUFFIX_REGEX, "");
    }

    private String[] parseUrl(String url) {
        try {
            URL parsedUrl = new URL(url);
            String path = parsedUrl.getPath();
            validateHost(parsedUrl.getHost());
            return parsePath(path);
        } catch (MalformedURLException e) {
            throw new GssException(ErrorCode.MALFORMED_GITHUB_REPOSITORY_URL);
        }
    }

    private void validateHost(String host) {
        if (!GITHUB_HOST_URL.equals(host)) {
            throw new GssException(ErrorCode.MALFORMED_GITHUB_REPOSITORY_URL);
        }
    }

    private String[] parsePath(String path) {
        if (path == null || path.isEmpty()) {
            throw new GssException(ErrorCode.MALFORMED_GITHUB_REPOSITORY_URL);
        }
        String[] pathSegments = path.replaceAll("^/+", "")
                .replaceAll("/+$", "")
                .split(PATH_DELIMITER);
        if (pathSegments.length < 2) {
            throw new GssException(ErrorCode.MALFORMED_GITHUB_REPOSITORY_URL);
        }
        return pathSegments;
    }
}
