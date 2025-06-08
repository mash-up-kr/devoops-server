package com.devoops.jpa.entity.github;

import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.user.User;
import com.devoops.jpa.entity.user.UserEntity;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "repositories")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GithubRepositoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    @NotNull
    private String name;

    @NotNull
    private String url;

    private long githubRepositoryId;

    public GithubRepository toDomainEntity() {
        return new GithubRepository(
                this.user.getId(),
                this.name,
                this.url,
                this.githubRepositoryId
        );
    }

    public static GithubRepositoryEntity from(GithubRepository domainEntity, UserEntity userEntity) {
        return new GithubRepositoryEntity(
                null,
                userEntity,
                domainEntity.getName(),
                domainEntity.getUrl(),
                domainEntity.getExternalId()
        );
    }
}
