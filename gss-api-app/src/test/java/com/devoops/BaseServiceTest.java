package com.devoops;

import com.devoops.generator.AnswerGenerator;
import com.devoops.generator.AnswerRankingGenerator;
import com.devoops.generator.GithubRepoGenerator;
import com.devoops.generator.PullRequestGenerator;
import com.devoops.generator.QuestionGenerator;
import com.devoops.generator.UserGenerator;
import com.devoops.generator.WebhookGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"test", "ci"})
@ExtendWith(DataBaseCleaner.class)
@Import(RedisTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public abstract class BaseServiceTest {

    @Autowired
    protected UserGenerator userGenerator;

    @Autowired
    protected GithubRepoGenerator repoGenerator;

    @Autowired
    protected PullRequestGenerator pullRequestGenerator;

    @Autowired
    protected QuestionGenerator questionGenerator;

    @Autowired
    protected AnswerGenerator answerGenerator;

    @Autowired
    protected AnswerRankingGenerator answerRankingGenerator;

    @Autowired
    protected WebhookGenerator webhookGenerator;
}

