package com.devoops.jpa.entity.github;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.RecordStatus;
import com.devoops.jpa.entity.BaseTimeEntity;
import com.devoops.jpa.entity.user.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String tag;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String summary;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String summaryDetail;

    private long githubPullRequestId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RecordStatus recordStatus;

    @NotNull
    private LocalDateTime mergedAt;

    public static PullRequestEntity from(
        PullRequest pullRequest,
        GithubRepositoryEntity repository
    ) {
        return new PullRequestEntity(
            pullRequest.getId(),
            repository,
            repository.getUser(),
            pullRequest.getTitle(),
            pullRequest.getDescription(),
            pullRequest.getTag(),
            pullRequest.getSummary(),
            pullRequest.getSummaryDetail(),
            pullRequest.getExternalId(),
            pullRequest.getRecordStatus(),
            pullRequest.getMergedAt()
        );
    }

    public void updateToDone() {
        this.recordStatus = RecordStatus.DONE;
    }

    public void updateStatus(RecordStatus recordStatus) {
        this.recordStatus = recordStatus;
    }

    public void updateAnalyzeResult(String summary, String summaryDetail) {
        this.summary = summary;
        this.summaryDetail = summaryDetail;
    }

    public PullRequest toDomainEntity() {
        return new PullRequest(
            this.id,
            this.repository.getId(),
            this.user.getId(),
            this.title,
            this.description,
            this.summary,
            this.summaryDetail,
            this.githubPullRequestId,
            this.recordStatus,
            this.mergedAt,
            this.tag
        );
    }
}
