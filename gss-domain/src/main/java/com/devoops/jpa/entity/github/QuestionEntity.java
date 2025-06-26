package com.devoops.jpa.entity.github;

import com.devoops.domain.entity.github.Question;
import com.devoops.jpa.entity.BaseTimeEntity;
import jakarta.persistence.Column;
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

@Entity
@Getter
@Table(name = "questions")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pull_request_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PullRequestEntity pullRequest;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean isAnswered;

    public Question toDomainEntity() {
        return new Question(
                this.id,
                pullRequest.getId(),
                this.content,
                this.isAnswered
        );
    }
}
