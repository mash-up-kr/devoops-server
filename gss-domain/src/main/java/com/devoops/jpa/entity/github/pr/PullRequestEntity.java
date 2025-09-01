package com.devoops.jpa.entity.github.pr;

import com.devoops.domain.entity.github.pr.ProcessingStatus;
import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.pr.RecordStatus;
import com.devoops.jpa.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(name = "pull_requests")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PullRequestEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long repositoryId;

    private long userId;

    @NotNull
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String tag;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String summaryDetail;

    private long githubPullRequestId;

    @NotNull
    private String pullRequestUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RecordStatus recordStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ColumnDefault("DONE")
    private ProcessingStatus processingStatus;

    @NotNull
    private LocalDateTime mergedAt;

    public static PullRequestEntity from(PullRequest pullRequest) {
        return new PullRequestEntity(
                pullRequest.getId(),
                pullRequest.getRepositoryId(),
                pullRequest.getUserId(),
                pullRequest.getTitle(),
                pullRequest.getDescription(),
                pullRequest.getTag(),
                pullRequest.getSummary(),
                pullRequest.getSummaryDetail(),
                pullRequest.getExternalId(),
                pullRequest.getPullRequestUrl(),
                pullRequest.getRecordStatus(),
                pullRequest.getProcessingStatus(),
                pullRequest.getMergedAt()
        );
    }

    public void updateStatus(RecordStatus recordStatus) {
        this.recordStatus = recordStatus;
    }

    public void updateAnalyzeResult(String summary, String summaryDetail) {
        this.processingStatus = ProcessingStatus.DONE;
        this.summary = summary;
        this.summaryDetail = summaryDetail;
    }

    public PullRequest toDomainEntity() {
        return new PullRequest(
                this.id,
                this.repositoryId,
                this.userId,
                this.title,
                this.description,
                this.summary,
                this.summaryDetail,
                this.pullRequestUrl,
                this.githubPullRequestId,
                this.recordStatus,
                this.processingStatus,
                this.mergedAt,
                this.tag
        );
    }
}
