package com.devoops.service.question;

import com.devoops.command.request.AnswerUpdateCommand;
import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.Answers;
import com.devoops.domain.entity.github.Question;
import com.devoops.domain.entity.github.QuestionAnswer;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.AnswerDomainRepository;
import com.devoops.domain.repository.github.QuestionDomainRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionDomainRepository questionRepository;
    private final AnswerDomainRepository answerRepository;

    public Answer initializeAnswer(long questionId, User user) {
        Question question = questionRepository.findById(questionId); //소유권 검증 추가
        return answerRepository.findByQuestionId(questionId)
                .orElseGet(() -> answerRepository.save(Answer.initialize(question.getId())));
    }

    public List<QuestionAnswer> getAllPrQuestions(long pullRequestsId) {
        return questionRepository.findAllPrQuestions(pullRequestsId);
    }

    public Answer updateAnswer(long answerId, String updateContent) {
        return answerRepository.updateById(answerId, updateContent);
    }

    public Answers updateAllAnswers(List<AnswerUpdateCommand> updateCommands) {
        List<Answer> answers = new ArrayList<>();
        for (AnswerUpdateCommand request : updateCommands) {
            Answer uppdatedAnswer = answerRepository.updateById(request.answerId(), request.content());
            answers.add(uppdatedAnswer); //TODO 쿼리 반복호출 말고, in절을 활용해 쿼리 2개로 줄이기
        }
        return new Answers(answers);
    }

    public void deleteAnswer(long answerId) {
        answerRepository.deleteById(answerId);
    }
}
