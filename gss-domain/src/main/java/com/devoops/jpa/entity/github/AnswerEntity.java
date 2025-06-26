package com.devoops.jpa.entity.github;

import com.devoops.domain.entity.github.Answer;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@Table(name = "answers")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private QuestionEntity question;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static AnswerEntity from(Answer answer, QuestionEntity question) {
        return new AnswerEntity(
                answer.getId(),
                question,
                answer.getContent()
        );
    }

    public AnswerEntity(Long id, QuestionEntity question, String content) {
        this.id = id;
        this.question = question;
        this.content = content;
    }

    public Answer toDomainEntity() {
        return new Answer(
                this.id,
                question.getId(),
                content
        );
    }
}
