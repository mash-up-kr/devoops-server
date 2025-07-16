package com.devoops.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class GithubPRUrlTest {

    @Nested
    class Parsed {

        @Test
        void 웹_상으로_이동가능한_풀_리퀘스트_url을_가진다() {
            String raw = "https://api.github.com/repos/owner/repo/pulls/38";
            String parsed = "https://github.com/owner/repo/pull/38";

            GithubPRUrl url = new GithubPRUrl(raw);

            assertThat(url.getValue()).isEqualTo(parsed);
        }
    }

}
