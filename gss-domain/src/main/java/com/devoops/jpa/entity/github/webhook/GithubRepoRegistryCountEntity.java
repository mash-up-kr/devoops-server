package com.devoops.jpa.entity.github.webhook;

import com.devoops.domain.entity.github.webhook.GithubRepoRegistryCount;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "github_repo_registry_count")
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GithubRepoRegistryCountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long externalId;

    private long registryCount;

    public static GithubRepoRegistryCountEntity from(GithubRepoRegistryCount githubRepoRegistryCount) {
        return new GithubRepoRegistryCountEntity(
                null,
                githubRepoRegistryCount.getExternalId(),
                githubRepoRegistryCount.getReposCount()
        );
    }

    public GithubRepoRegistryCount toDomainEntity() {
        return new GithubRepoRegistryCount(this.externalId, this.registryCount);
    }

    public void plusCount() {
        this.registryCount += 1;
    }

    public void minusCount() {
        this.registryCount -= 1;
    }
}
