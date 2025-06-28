package com.devoops.service.facade;

import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.user.User;
import com.devoops.service.GitHubService;
import com.devoops.service.repository.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepositoryFacadeService {

    private final RepositoryService repositoryService;
    private final GitHubService gitHubService;

    public GithubRepository save(String repositoryUrl, User user) {
        //TODO 레포지토리 정보 가져와서 저장
        //TODO 레포지토리에 웹훅 심기

        return null;

    }
}
