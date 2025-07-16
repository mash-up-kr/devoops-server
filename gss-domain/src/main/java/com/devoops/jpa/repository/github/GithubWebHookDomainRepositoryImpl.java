package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.GithubWebhook;
import com.devoops.domain.repository.github.GithubWebhookDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.jpa.entity.github.GithubWebhookEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class GithubWebHookDomainRepositoryImpl implements GithubWebhookDomainRepository {

    private final GithubWebHookJpaRepository webHookRepository;

    @Override
    @Transactional
    public GithubWebhook save(GithubWebhook webHook) {
        GithubWebhookEntity webHookEntity = GithubWebhookEntity.from(webHook);
        return webHookRepository.save(webHookEntity)
                .toDomainEntity();
    }

    @Override
    @Transactional(readOnly = true)
    public GithubWebhook findByRepositoryId(long repositoryId) {
         return webHookRepository.findByRepositoryId(repositoryId)
                .orElseThrow(() -> new GssException(ErrorCode.WEBHOOK_NOT_FOUND))
                 .toDomainEntity();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        webHookRepository.deleteById(id);
    }
}
