package com.devoops.fake;

import com.devoops.domain.repository.auth.BlackListRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakeBlackListRepository implements BlackListRepository {

    private final List<String> blackList = new ArrayList<>();

    @Override
    public boolean isExists(String token) {
        return blackList.stream()
                .anyMatch(blackToken -> blackToken.equals(token));
    }

    @Override
    public void addBlackList(String token, Date expiration) {
        blackList.add(token);
    }

    public void clear() {
        blackList.clear();
    }
}
