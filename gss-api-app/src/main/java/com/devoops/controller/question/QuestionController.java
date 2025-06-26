package com.devoops.controller.question;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {

    @PostMapping("/api/questions/{questionId}/answer")
    public ResponseEntity<Void> createAnswer() {
        return null;
    }
}
