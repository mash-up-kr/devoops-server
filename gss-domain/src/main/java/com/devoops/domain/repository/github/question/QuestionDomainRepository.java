package com.devoops.domain.repository.github.question;

import com.devoops.domain.entity.github.pr.PullRequests;
import com.devoops.domain.entity.github.question.Question;
import com.devoops.domain.entity.github.QuestionAnswer;
import java.util.List;

public interface QuestionDomainRepository {

    Question save(Question question);

    void saveAll(List<Question> questions, long pullRequestId);

    Question findById(long questionId);

    List<QuestionAnswer> findAllPrQuestions(long pullRequestId);

    List<Question> findAllByPullRequests(PullRequests repositoryPrs);

    void deleteAll(List<Question> questions);
}
