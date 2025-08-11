package com.devoops.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.devoops.BaseServiceTest;
import com.devoops.client.GitHubClient;
import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.github.webhook.GithubWebhook;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.webhook.GithubWebhookDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class GitHubServiceTest extends BaseServiceTest {

    @MockitoBean
    private GitHubClient gitHubClient;

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private GithubWebhookDomainRepository webhookDomainRepository;


    @Nested
    class DeleteWebHook {

        @Test
        void 웹_훅을_삭제_할_수_있다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            GithubWebhook webhook = webhookGenerator.generate(user, repo);

            gitHubService.deleteWebhook(user, repo.getId());

            assertThatThrownBy(() -> webhookDomainRepository.findByRepositoryId(repo.getId()))
                    .isInstanceOf(GssException.class)
                    .hasMessage(ErrorCode.WEBHOOK_NOT_FOUND.getMessage());
        }
    }
}
