package com.devoops.domain.notifier;

import com.devoops.exception.message.ErrorMessageResolver;
import com.devoops.exception.notifier.NotifyPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ErrorNotifier {

    private final NotifyPort notifyPort;

    public void notify(Throwable throwable) {
        String message = ErrorMessageResolver.resolve(throwable);
        notifyPort.sendMessage(message);
    }
}
