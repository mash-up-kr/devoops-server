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

    public Answer initializeAnswer(long questionId, User user) {
        Question question = questionRepository.findById(questionId); //소유권 검증 추가
        return answerRepository.save(Answer.initialize(question.getId()));
    }

    public Answer updateAnswer(long answerId, String updateContent) {
        return answerRepository.updateById(answerId, updateContent);
    }

    public void deleteAnswer(long answerId) {
        answerRepository.deleteById(answerId);
    }
}
