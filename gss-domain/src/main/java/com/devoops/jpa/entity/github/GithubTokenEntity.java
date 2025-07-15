package com.devoops.jpa.entity.github;

import com.devoops.domain.entity.github.GithubToken;
import com.devoops.jpa.entity.user.UserEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "github_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GithubTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long userId;

    @Nonnull
    @Column(length = 50)
    private String token;

    public static GithubTokenEntity from(long userId, GithubToken githubToken) {
        return new GithubTokenEntity(
                null,
                userId,
                githubToken.getToken()
        );
    }

    public GithubToken toDomainEntity() {
        return new GithubToken(this.token);
    }
}
