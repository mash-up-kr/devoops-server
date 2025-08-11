package com.devoops.jpa.entity.github.question;

import com.devoops.domain.entity.github.question.Question;
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
@Table(name = "questions")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long pullRequestId;

    @NotNull
    private String category;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean isAnswered;

    public static QuestionEntity from(Question question) {
        return new QuestionEntity(question.getId(), question.getPullRequestId(), question.getCategory(), question.getContent(),
            question.isAnswered());
    }

    public Question toDomainEntity() {
        return new Question(
            this.id,
            pullRequestId,
            this.category,
            this.content,
            this.isAnswered
        );
    }
}
