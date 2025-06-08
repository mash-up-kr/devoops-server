package com.devoops.jpa.entity.github;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.RecordStatus;
import com.devoops.jpa.entity.BaseTimeEntity;
import com.devoops.jpa.entity.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "pull_requests")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PullRequestEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private GithubRepositoryEntity repository;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    @NotNull
    private String title;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String summary;

    private long githubPullRequestId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RecordStatus recordStatus;

    @NotNull
    private LocalDateTime mergedAt;

    public PullRequest toDomainEntity() {
        return new PullRequest(
                this.repository.getId(),
                this.user.getId(),
                this.title,
                this.description,
                this.summary,
                this.githubPullRequestId,
                this.recordStatus,
                this.mergedAt
        );
    }
}
