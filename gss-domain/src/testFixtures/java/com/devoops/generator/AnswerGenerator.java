package com.devoops.generator;

import com.devoops.domain.entity.github.answer.Answer;
import com.devoops.domain.entity.github.question.Question;
import com.devoops.domain.repository.github.answer.AnswerDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnswerGenerator {

    @Autowired
    private AnswerDomainRepository answerDomainRepository;

    public Answer generate(Question question, String content) {
        Answer answer = new Answer(null, question.getId(), content);
        return answerDomainRepository.save(answer);
    }
}
