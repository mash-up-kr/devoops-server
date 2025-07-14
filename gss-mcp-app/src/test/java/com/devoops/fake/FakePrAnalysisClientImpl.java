package com.devoops.fake;

import com.devoops.client.PrAnalysisClientImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakePrAnalysisClientImpl extends PrAnalysisClientImpl {


    public FakePrAnalysisClientImpl() {
        super(Mockito.mock(ChatModel), new ObjectMapper());
    }
}
