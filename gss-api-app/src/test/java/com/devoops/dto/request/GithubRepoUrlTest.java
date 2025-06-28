package com.devoops.dto.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GithubRepoUrlTest {


    @Nested
    class ParseUrl {

        @ParameterizedTest
        @ValueSource(strings = {
                "https://github.com/octocat/Hello-World.git",
                "https://github.com/octocat/Hello-World/",
                "https://github.com/octocat/Hello-World?tab=readme"
        })
        void 깃허브_url을_파싱한다(String url) {
            GithubRepoUrl parsedUrl = new GithubRepoUrl(url);

            assertAll(
                    () -> assertThat(parsedUrl.getOwner()).isEqualTo("octocat"),
                    () -> assertThat(parsedUrl.getRepoName()).isEqualTo("Hello-World")
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "https://naver.com/octocat/Hello-World.git",
                "https://daum.net/octocat/Hello-World/sdf",
        })
        void 잘못된_url은_오류를_발생시킨다(String invalidUrl) {
            assertThatThrownBy(() -> new GithubRepoUrl(invalidUrl))
                    .isInstanceOf(GssException.class)
                    .hasMessage(ErrorCode.MALFORMED_GITHUB_REPOSITORY_URL.getMessage());
        }
    }
}
