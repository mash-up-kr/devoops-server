package com.devoops.jpa.entity.github.repo;

import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.jpa.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "repositories")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GithubRepositoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long userId;

    @NotNull
    private String name;

    @NotNull
    private String url;

    @NotNull
    private String owner;

    @Column(name = "is_tracking")
    private boolean isTracking;

    private int pullRequestCount;

    private long githubRepositoryId;

    public static GithubRepositoryEntity from(GithubRepository githubRepository) {

        return new GithubRepositoryEntity(
                githubRepository.getId(),
                githubRepository.getUserId(),
                githubRepository.getName(),
                githubRepository.getUrl(),
                githubRepository.getOwner(),
                githubRepository.isTracking(),
                githubRepository.getPrCount(),
                githubRepository.getExternalId()
        );
    }

    public GithubRepositoryEntity update(GithubRepository githubRepository) {
        this.id = githubRepository.getId();
        this.userId = githubRepository.getUserId();
        this.name = githubRepository.getName();
        this.url = githubRepository.getUrl();
        this.owner = githubRepository.getOwner();
        this.pullRequestCount = githubRepository.getPrCount();
        this.githubRepositoryId = githubRepository.getExternalId();
        this.isTracking = githubRepository.isTracking();
        return this;
    }

    public GithubRepository toDomainEntity() {
        return new GithubRepository(
                this.id,
                this.userId,
                this.name,
                this.url,
                this.owner,
                this.pullRequestCount,
                this.githubRepositoryId,
                this.isTracking
                );
    }

    public void plusPrCount() {
        this.pullRequestCount++;
    }
}
