package com.devoops.domain.entity.github;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Answers {

    private final List<Answer> values;
}
