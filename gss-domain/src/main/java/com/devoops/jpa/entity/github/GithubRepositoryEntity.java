package com.devoops.jpa.entity.github;

import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.jpa.entity.BaseTimeEntity;
import com.devoops.jpa.entity.user.UserEntity;
import jakarta.persistence.*;
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

    private int pullRequestCount;

    @Column(unique = true)
    private long githubRepositoryId;

    public static GithubRepositoryEntity from(GithubRepository githubRepository) {

        return new GithubRepositoryEntity(
            githubRepository.getId(),
            githubRepository.getUserId(),
            githubRepository.getName(),
            githubRepository.getUrl(),
            githubRepository.getOwner(),
            githubRepository.getPrCount(),
            githubRepository.getExternalId()
        );
    }

    public GithubRepository toDomainEntity() {
        return new GithubRepository(
            this.id,
            this.userId,
            this.name,
            this.url,
            this.owner,
            this.pullRequestCount,
            this.githubRepositoryId
        );
    }

    public void plusPrCount() {
        this.pullRequestCount++;
    }
}
