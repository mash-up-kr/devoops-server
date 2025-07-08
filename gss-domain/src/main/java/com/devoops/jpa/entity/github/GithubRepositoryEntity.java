package com.devoops.jpa.entity.github;

import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.jpa.entity.BaseTimeEntity;
import com.devoops.jpa.entity.user.UserEntity;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.devoops.Constants.INITIAL_PULL_REQUEST_COUNT;

@Entity
@Getter
@Table(name = "repositories")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GithubRepositoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    @NotNull
    private String name;

    @NotNull
    private String url;

    @NotNull
    private String owner;

    private int pullRequestCount;

    private long githubRepositoryId;

    public static GithubRepositoryEntity from(GithubRepository githubRepository, UserEntity user) {

        return new GithubRepositoryEntity(
            githubRepository.getId(),
            user,
            githubRepository.getName(),
            githubRepository.getUrl(),
            githubRepository.getName(),
            INITIAL_PULL_REQUEST_COUNT,
            githubRepository.getExternalId()
        );
    }

    public GithubRepository toDomainEntity() {
        return new GithubRepository(
            this.id,
            this.user.getId(),
            this.name,
            this.url,
            this.owner,
            this.pullRequestCount,
            this.githubRepositoryId
        );
    }
}
