package com.devoops.controller.question;

import com.devoops.controller.auth.AuthUser;
import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.Answers;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.AnswerPutRequests;
import com.devoops.dto.request.AnswerUpdateRequest;
import com.devoops.dto.response.AnswerPutResponses;
import com.devoops.dto.response.AnswerSaveResponse;
import com.devoops.dto.response.AnswerUpdateResponse;
import com.devoops.service.facade.QuestionFacadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionFacadeService questionFacadeService;

    @PostMapping("/api/questions/{questionId}/answer")
    public ResponseEntity<AnswerSaveResponse> createAnswer(
            @AuthUser User user,
            @PathVariable(name = "questionId") long questionId
    ) {
        Answer answer = questionFacadeService.initializeAnswer(questionId, user);
        AnswerSaveResponse response = new AnswerSaveResponse(answer);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/questions/answer")
    public ResponseEntity<AnswerPutResponses> updateAllAnswer(
            @AuthUser User user,
            @Valid @RequestBody AnswerPutRequests request
    ) {
        Answers updatedAnswers = questionFacadeService.updateAllAnswers(request);
        AnswerPutResponses response = AnswerPutResponses.from(updatedAnswers);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/questions/answer/{answerId}")
    public ResponseEntity<AnswerUpdateResponse> updateAnswer(
            @AuthUser User user,
            @PathVariable(name = "answerId") long answerId,
            @Valid @RequestBody AnswerUpdateRequest request
    ) {

        //TODO ranking 갱신
        Answer updatedAnswer = questionFacadeService.updateAnswer(answerId, request.content(), user.getId());
        AnswerUpdateResponse response = new AnswerUpdateResponse(updatedAnswer);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/questions/answer/{answerId}")
    public ResponseEntity<Void> deleteAnswer(
            @AuthUser User user,
            @PathVariable(name = "answerId") long answerId
    ) {
        questionFacadeService.deleteAnswer(answerId);
        return ResponseEntity.noContent().build();
    }
}
