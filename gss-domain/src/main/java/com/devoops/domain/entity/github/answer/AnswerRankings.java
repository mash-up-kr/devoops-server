package com.devoops.domain.entity.github.answer;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AnswerRankings {

    private static final int FULL_THRESHOLD = 3;

    private final List<AnswerRanking> rankings;

    public boolean isFull() {
        return rankings.size() == FULL_THRESHOLD;
    }

    public Optional<AnswerRanking> getSamePrRanking(long pullRequestId) {
        return rankings.stream()
                .filter(ranking -> ranking.getPullRequestId() == pullRequestId)
                .findAny();
    }

    public AnswerRanking getLastUsedRanking() {
        return rankings.stream()
                .min(Comparator.comparing(AnswerRanking::getUpdatedAt))
                .orElseThrow(() -> new RuntimeException("회고가 존재하지 않습니다"));
    }
}
