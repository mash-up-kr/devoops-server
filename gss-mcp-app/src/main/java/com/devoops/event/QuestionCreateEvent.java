package com.devoops.event;

import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.github.PullRequest;
import com.devoops.dto.AppWebhookEventRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class QuestionCreateEvent extends ApplicationEvent {

    private final AppWebhookEventRequest request;
    private final PullRequest initializedPullRequest;
    private final GithubToken token;


    public QuestionCreateEvent(
            Object source,
            AppWebhookEventRequest request,
            PullRequest initializedPullRequest,
            GithubToken token
    ) {
        super(source);
        this.request = request;
        this.initializedPullRequest = initializedPullRequest;
        this.token = token;
    }
}
