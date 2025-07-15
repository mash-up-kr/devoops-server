package com.devoops.command.request;

public record AnswerUpdateCommand(
        long answerId,
        String content
) {

}
