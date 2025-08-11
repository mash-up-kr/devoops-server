package com.devoops.service.github;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;

import com.devoops.BaseServiceTest;
import com.devoops.client.GitHubClient;
import com.devoops.domain.entity.github.webhook.GithubRepoRegistryCount;
import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.webhook.GithubRepoRegistryCountRepository;
import com.devoops.domain.repository.github.webhook.GithubWebhookDomainRepository;
import com.devoops.dto.response.WebHookCreateResponse;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class WebHookServiceTest extends BaseServiceTest {

    @MockitoBean
    private GitHubClient gitHubClient;

    @Autowired
    private WebHookService webHookService;

    @Autowired
    private GithubWebhookDomainRepository webhookDomainRepository;

    @Autowired
    private GithubRepoRegistryCountRepository registryCountRepository;

    @Nested
    class RegisterWebHook {

        @Test
        void 웹훅을_최초_등록할_수_있다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            Mockito.when(gitHubClient.createWebhook(anyString(), anyString(), anyString(), any()))
                    .thenReturn(new WebHookCreateResponse(1234L));

            webHookService.registerWebhook(user, repo.getId());

            Mockito.verify(gitHubClient, Mockito.times(1))
                    .createWebhook(anyString(), anyString(), anyString(), any());
        }

        @Test
        void 이미_웹훅이_등록된_레포지토리는_등록_카운트를_증가시킨다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            Mockito.when(gitHubClient.createWebhook(anyString(), anyString(), anyString(), any()))
                    .thenReturn(new WebHookCreateResponse(1234L));
            registryCountRepository.save(new GithubRepoRegistryCount(repo.getExternalId(), 1L));

            webHookService.registerWebhook(user, repo.getId());

            GithubRepoRegistryCount registryCount = registryCountRepository.getByExternalId(repo.getExternalId());
            assertAll(
                    () -> Mockito.verify(gitHubClient, never())
                            .createWebhook(anyString(), anyString(), anyString(), any()),
                    () -> assertThat(registryCount.getReposCount()).isEqualTo(2L)
            );
        }
    }


    @Nested
    class DeleteWebHook {

        @Test
        void 웹_훅을_삭제_할_수_있다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            webhookGenerator.generate(user, repo);
            registryCountRepository.save(new GithubRepoRegistryCount(repo.getExternalId(), 1L));

            webHookService.deleteWebhook(user, repo.getId());

            boolean isExists = registryCountRepository.existsByExternalId(repo.getExternalId());
            assertAll(
                    () -> assertThatThrownBy(() -> webhookDomainRepository.findByRepositoryId(repo.getId()))
                            .isInstanceOf(GssException.class)
                            .hasMessage(ErrorCode.WEBHOOK_NOT_FOUND.getMessage()),
                    () -> assertThat(isExists).isFalse()
            );
        }

        @Test
        void 여러_유저가_트래킹하는_경우_웹훅_등록_카운트를_감소시킨다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            webhookGenerator.generate(user, repo);
            registryCountRepository.save(new GithubRepoRegistryCount(repo.getExternalId(), 2L));

            webHookService.deleteWebhook(user, repo.getId());

            GithubRepoRegistryCount registryCount = registryCountRepository.getByExternalId(repo.getExternalId());
            assertAll(
                    () -> assertThatCode(() -> webhookDomainRepository.findByRepositoryId(repo.getId()))
                            .doesNotThrowAnyException(),
                    () -> assertThat(registryCount.getReposCount()).isEqualTo(1L)
            );
        }
    }
}

