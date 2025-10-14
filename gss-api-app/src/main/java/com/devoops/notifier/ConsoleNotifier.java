package com.devoops.notifier;


public class ConsoleNotifier implements NotifyPort {

    @Override
    public void sendMessage(String message) {
        System.out.println("[메시지 발송] : " + message);
    }
}
