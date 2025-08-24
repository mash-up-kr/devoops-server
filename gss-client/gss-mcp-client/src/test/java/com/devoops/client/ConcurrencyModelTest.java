package com.devoops.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.ai.openai.OpenAiChatOptions;

@Disabled
class ConcurrencyModelTest {

    private final OpenAiChatOptions.Builder sharedBuilder = OpenAiChatOptions.builder();

    @Test
    void 동시성_이슈에_따라_옵션이_섞이지_않아야_한다() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<String>> results = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            final int id = i;
            results.add(executor.submit(() -> {
                sharedBuilder.model(buildModel(id));
                return "id = " + id + " model = " + sharedBuilder.build().getModel();
            }));
        }

        for (Future<String> f : results) {
            System.out.println(f.get());
        }
    }

    private String buildModel(int id) {
        if (id % 2 == 0) {
            return "gpt-5-mini";
        }
        return "gpt-5";
    }
}
