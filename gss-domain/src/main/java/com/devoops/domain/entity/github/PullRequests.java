package com.devoops.domain.entity.github;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PullRequests {

    private final List<PullRequest> values;
}
