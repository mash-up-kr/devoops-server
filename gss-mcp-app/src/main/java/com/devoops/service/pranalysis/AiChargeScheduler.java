package com.devoops.service.pranalysis;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AiChargeScheduler {

    private final AiChargeService aiChargeService;

    @Scheduled(cron = "0 55 23 L * ?", zone = "Asia/Seoul")
    public void createNextMonthCharge() {
        aiChargeService.initializeNextMonth();
    }
}
