package com.devoops.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public record GithubPrResponse(
        long id,
        String title,
        String body,
        User user,
        Repository repo,
        List<Label> labels,
        @JsonProperty("diff_url") String diffUrl,
        @JsonProperty("merged_at") LocalDateTime mergedAt

) {

    public record User (
            long id,
            String login
    ) { }

    public record Repository (
            long id,
            String login
    ) { }

    public record Label(
            long id,
            String name
    ) {
    }

    public long getRepositoryId() {
        return repo.id;
    }

    public Long getUserId() {
        return user.id;
    }


    public String getDescription() {
        return body;
    }

    public long getExternalId() {
        return id;
    }

    public String getTag() {
        if(labels.isEmpty()) return "NONE";
        return labels.getFirst().name;
    }
}
