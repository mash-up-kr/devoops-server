package com.devoops.controller.question;

import com.devoops.controller.auth.AuthUser;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.response.AnswerSaveResponse;
import com.devoops.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/api/questions/{questionId}/answer")
    public ResponseEntity<AnswerSaveResponse> createAnswer(
            @AuthUser User user,
            @PathVariable(name = "questionId") long questionId
    ) {
        return null;
    }
}
