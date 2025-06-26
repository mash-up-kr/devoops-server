package com.devoops.controller.question;

import com.devoops.controller.auth.AuthUser;
import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.AnswerUpdateRequest;
import com.devoops.dto.response.AnswerSaveResponse;
import com.devoops.dto.response.AnswerUpdateResponse;
import com.devoops.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        Answer answer = questionService.initializeAnswer(questionId, user);
        AnswerSaveResponse response = new AnswerSaveResponse(answer);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/questions/answer/{answerId}")
    public ResponseEntity<AnswerUpdateResponse> updateAnswer(
            @AuthUser User user,
            @PathVariable(name = "answerId") long answerId,
            @RequestBody AnswerUpdateRequest request
    ) {
        Answer updatedAnswer = questionService.updateAnswer(answerId, request.content());
        AnswerUpdateResponse response = new AnswerUpdateResponse(updatedAnswer);
        return ResponseEntity.ok(response);
    }
}
