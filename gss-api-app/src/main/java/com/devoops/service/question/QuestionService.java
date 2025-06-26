package com.devoops.service.question;

import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.Question;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.AnswerDomainRepository;
import com.devoops.domain.repository.github.QuestionDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionDomainRepository questionRepository;
    private final AnswerDomainRepository answerRepository;

    public Answer saveAnswer(long questionId, User user) {
        Question question = questionRepository.findByIdAndUserId(questionId, user.getId());
        return answerRepository.save(Answer.initialize(question.getId()));
    }
}
