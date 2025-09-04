package com.devoops.event;

import com.devoops.domain.entity.github.answer.Answer;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateAnswerEvent extends ApplicationEvent {

    private final Answer answer;
    private final long userId;

    public UpdateAnswerEvent(Object source, Answer answer, long userId) {
        super(source);
        this.answer = answer;
        this.userId = userId;
    }
}
