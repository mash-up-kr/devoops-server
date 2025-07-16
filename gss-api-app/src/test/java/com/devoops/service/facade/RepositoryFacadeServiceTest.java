package com.devoops.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

import com.devoops.BaseServiceTest;
import com.devoops.client.GitHubClient;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubRepoDomainRepository;
import com.devoops.dto.request.RepositorySaveRequest;
import com.devoops.dto.response.GithubRepoInfoResponse;
import com.devoops.dto.response.OwnerResponse;
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
            GithubRepoInfoResponse mockResponse = new GithubRepoInfoResponse(123, "testName", "testUrl",
                    new OwnerResponse("김건우"));
            Mockito.when(gitHubClient.getRepositoryInfo(anyString(), anyString(), anyString()))
                    .thenReturn(mockResponse);

            GithubRepository savedRepository = repositoryFacadeService.save(request, user);

            boolean exists = githubRepoDomainRepository.existsByIdAndUserId(savedRepository.getId(), user.getId());

            assertAll(
                    () -> assertThat(exists).isTrue(),
                    () -> Mockito.verify(gitHubClient, times(1)).createWebhook(any(), any(), any(), any()),
                    () -> Mockito.verify(gitHubClient, times(1)).getRepositoryInfo(any(), any(), any())
            );
        }

        @Test
        void 레포지토리를_중복_저장할_수_없다() {
            User user = userGenerator.generate("김건우");
            RepositorySaveRequest request = new RepositorySaveRequest("https://github.com/octocat/Hello-World");
            GithubRepoInfoResponse mockResponse = new GithubRepoInfoResponse(123, "testName", "testUrl",
                    new OwnerResponse("김건우"));
            Mockito.when(gitHubClient.getRepositoryInfo(anyString(), anyString(), anyString()))
                    .thenReturn(mockResponse);
            repositoryFacadeService.save(request, user);

            assertThatThrownBy(() -> repositoryFacadeService.save(request, user))
                    .isInstanceOf(GssException.class)
                    .hasMessage(ErrorCode.ALREADY_SAVED_REPOSITORY.getMessage());
        }
    }
}
