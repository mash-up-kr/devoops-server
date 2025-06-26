package com.devoops.service.question;

import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    public Answer saveAnswer(long questionId, User user) {
        return null;
    }
}
