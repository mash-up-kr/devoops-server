package com.devoops.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

import com.devoops.BaseServiceTest;
import com.devoops.client.GitHubClient;
import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.repo.GithubRepoDomainRepository;
import com.devoops.dto.request.RepositorySaveRequest;
import com.devoops.dto.response.GithubRepoInfoResponse;
import com.devoops.dto.response.OwnerResponse;
import com.devoops.dto.response.WebHookCreateResponse;
import com.devoops.exception.GithubForbiddenException;
import com.devoops.exception.GithubNotFoundException;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class RepositoryFacadeServiceTest extends BaseServiceTest {

    @Autowired
    private GithubRepoDomainRepository githubRepoDomainRepository;

    @Autowired
    private RepositoryFacadeService repositoryFacadeService;

    @MockitoBean
    private GitHubClient gitHubClient;

    @Nested
    class Save {

        @Test
        void 레포지토리를_저장하고_웹훅을_심는다() {
            User user = userGenerator.generate("김건우");
            RepositorySaveRequest request = new RepositorySaveRequest("https://github.com/octocat/Hello-World");
            mockingGithubClient();

            GithubRepository savedRepository = repositoryFacadeService.save(request, user);

            boolean exists = githubRepoDomainRepository.existsByIdAndUserId(savedRepository.getId(), user.getId());

            assertAll(
                    () -> assertThat(exists).isTrue(),
                    () -> Mockito.verify(gitHubClient, times(1)).createWebhook(any(), any(), any(), any()),
                    () -> Mockito.verify(gitHubClient, times(1)).getRepositoryInfo(any(), any(), any())
            );
        }

        @Test
        void 웹훅_등록_시_404_에러가_발생하면_애플리케이션_에러로_전환한다() {
            User user = userGenerator.generate("김건우");
            RepositorySaveRequest request = new RepositorySaveRequest("https://github.com/octocat/Hello-World");
            mockingErrorWhenCreateWebHook(new GithubNotFoundException("mocking error"));

            assertThatThrownBy(() -> repositoryFacadeService.save(request, user))
                    .isInstanceOf(GssException.class)
                    .hasMessage(ErrorCode.REGISTRY_GITHUB_REPOSITORY_NOT_FOUND.getMessage());
        }

        @Test
        void 웹훅_등록_시_403_에러가_발생하면_애플리케이션_에러로_전환한다() {
            User user = userGenerator.generate("김건우");
            RepositorySaveRequest request = new RepositorySaveRequest("https://github.com/octocat/Hello-World");
            mockingErrorWhenCreateWebHook(new GithubForbiddenException("mocking error"));

            assertThatThrownBy(() -> repositoryFacadeService.save(request, user))
                    .isInstanceOf(GssException.class)
                    .hasMessage(ErrorCode.REGISTRY_GITHUB_REPOSITORY_NOT_FOUND.getMessage());
        }

        @Test
        void 동일_유저가_레포지토리를_중복_저장할_수_없다() {
            User user = userGenerator.generate("김건우");
            RepositorySaveRequest request = new RepositorySaveRequest("https://github.com/octocat/Hello-World");
            mockingGithubClient();

            repositoryFacadeService.save(request, user);

            assertThatThrownBy(() -> repositoryFacadeService.save(request, user))
                    .isInstanceOf(GssException.class)
                    .hasMessage(ErrorCode.ALREADY_SAVED_REPOSITORY.getMessage());
        }

        @Test
        void 다른_유저가_레포지토리를_중복_저장할_수_있다() {
            User user1 = userGenerator.generate("김건우1");
            User user2 = userGenerator.generate("김건우2");
            RepositorySaveRequest request = new RepositorySaveRequest("https://github.com/octocat/Hello-World");
            mockingGithubClient();

            assertThatCode(() -> {
                repositoryFacadeService.save(request, user1);
                repositoryFacadeService.save(request, user2);
            }).doesNotThrowAnyException();
        }

        @Test
        void 레포지토리를_재연결_할_수_있다() {
            User user = userGenerator.generate("김건우");
            GithubRepository unTrackingRepo = repoGenerator.generate(user, "연결 끊긴 레포지토리", 123L, false);
            RepositorySaveRequest request = new RepositorySaveRequest("https://github.com/octocat/Hello-World");
            mockingGithubClient();

            repositoryFacadeService.save(request, user);

            GithubRepository actual = githubRepoDomainRepository.findByIdAndUserId(
                    unTrackingRepo.getId(),
                    user.getId()
            );
            assertThat(actual.isTracking()).isTrue();
        }

        private void mockingGithubClient() {
            GithubRepoInfoResponse mockResponse = new GithubRepoInfoResponse(123, "testName", "testUrl",
                    new OwnerResponse("김건우"));
            WebHookCreateResponse mockWebHookCreateResponse = new WebHookCreateResponse(123);
            Mockito.when(gitHubClient.getRepositoryInfo(anyString(), anyString(), anyString()))
                    .thenReturn(mockResponse);
            Mockito.when(gitHubClient.createWebhook(any(), any(), any(), any()))
                    .thenReturn(mockWebHookCreateResponse);
        }

        private void mockingErrorWhenCreateWebHook(Exception exception) {
            GithubRepoInfoResponse mockResponse = new GithubRepoInfoResponse(123, "testName", "testUrl",
                    new OwnerResponse("김건우"));
            WebHookCreateResponse mockWebHookCreateResponse = new WebHookCreateResponse(123);
            Mockito.when(gitHubClient.getRepositoryInfo(anyString(), anyString(), anyString()))
                    .thenReturn(mockResponse);
            Mockito.when(gitHubClient.createWebhook(any(), any(), any(), any()))
                    .thenThrow(exception);
        }
    }

    @Nested
    class Delete {

        @Test
        void 웹훅을_찾지_못해도_레포지토리_트래킹을_끊을_수_있다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            webhookGenerator.generate(user, repo, 1L);
            Mockito.doThrow(new GithubNotFoundException("mocking error"))
                    .when(gitHubClient)
                    .deleteWebhook(anyString(), anyString(), anyString(), anyLong());

            repositoryFacadeService.deleteRepository(user, repo.getId());

            GithubRepository foundRepo = githubRepoDomainRepository.findByIdAndUserId(repo.getId(), user.getId());
            assertThat(foundRepo.isTracking()).isFalse();
        }
    }
}
