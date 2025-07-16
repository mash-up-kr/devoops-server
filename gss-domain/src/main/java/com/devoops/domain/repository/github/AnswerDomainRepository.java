package com.devoops.domain.repository.github;

import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.Question;
import java.util.List;

public interface AnswerDomainRepository {

    Answer save(Answer answer);

    Answer findById(long answerId);

    long getAnswerCountByPullRequestId(long pullRequestId);

    Answer updateById(long answerId, String content);

    void deleteById(long answerId);

    void deleteAllInQuestions(List<Question> questions);
}
