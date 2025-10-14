package com.devoops.domain.notifier;


import com.devoops.exception.notifier.NotifyPort;

public class ConsoleNotifier implements NotifyPort {

    @Override
    public void sendMessage(String message) {
        System.out.println("[메시지 발송] : " + message);
    }
}
