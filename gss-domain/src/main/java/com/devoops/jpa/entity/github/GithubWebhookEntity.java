package com.devoops.jpa.entity.github;

import com.devoops.domain.entity.github.GithubWebhook;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GithubWebhookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long externalId;

    private long repositoryId;

    public static GithubWebhookEntity from(GithubWebhook githubWebhook) {
        return new GithubWebhookEntity(
                githubWebhook.getId(),
                githubWebhook.getExternalId(),
                githubWebhook.getRepositoryId()
        );
    }

    public GithubWebhook toDomainEntity() {
        return new GithubWebhook(this.id, this.externalId, this.repositoryId);
    }
}
