package com.devoops.generator;

import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.question.Question;
import com.devoops.domain.repository.github.question.QuestionDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionGenerator {

    @Autowired
    private QuestionDomainRepository questionDomainRepository;

    public Question generate(PullRequest pullRequest, String content) {
        Question question = new Question(null, pullRequest.getId(), "category", content, false);
        return questionDomainRepository.save(question);
    }
}
