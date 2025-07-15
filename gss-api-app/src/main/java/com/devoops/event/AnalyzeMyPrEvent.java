package com.devoops.event;

import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.GithubRepoUrl;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AnalyzeMyPrEvent extends ApplicationEvent {

    private final GithubRepoUrl repoUrl;
    private final User user;

    public AnalyzeMyPrEvent(
            GithubRepoUrl repoUrl,
            User user,
            Object source
    ) {
        super(source);
        this.repoUrl = repoUrl;
        this.user = user;
    }
}
