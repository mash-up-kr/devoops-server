package com.devoops;

import static org.mockito.ArgumentMatchers.any;

import com.devoops.client.claude.ClaudePrAnalysisClient;
import com.devoops.client.openai.OpenAiPrAnalysisClient;
import com.devoops.dto.request.AnalyzePrRequest;
import com.devoops.fixture.PrAnalysisFixture;
import com.devoops.generator.AnswerGenerator;
import com.devoops.generator.AnswerRankingGenerator;
import com.devoops.generator.GithubRepoGenerator;
import com.devoops.generator.PullRequestGenerator;
import com.devoops.generator.QuestionGenerator;
import com.devoops.generator.UserGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ExtendWith(DataBaseCleaner.class)
@Import({RedisTestConfiguration.class})
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public abstract class BaseMcpTest {

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

    @MockitoBean
    protected OpenAiPrAnalysisClient openAiPrAnalysisClient;

    @MockitoBean
    protected ClaudePrAnalysisClient claudePrAnalysisClient;

    @BeforeEach
    void setUp() {
        Mockito.doReturn(PrAnalysisFixture.MOCK_RESPONSE)
                        .when(openAiPrAnalysisClient).analyze(any(AnalyzePrRequest.class));

        Mockito.doReturn(McpClientType.OPEN_AI)
                .when(openAiPrAnalysisClient).getMcpClientType();

        Mockito.doReturn(PrAnalysisFixture.MOCK_RESPONSE)
                .when(claudePrAnalysisClient).analyze(any(AnalyzePrRequest.class));

        Mockito.doReturn(McpClientType.CLAUDE)
                        .when(claudePrAnalysisClient).getMcpClientType();
    }
}
