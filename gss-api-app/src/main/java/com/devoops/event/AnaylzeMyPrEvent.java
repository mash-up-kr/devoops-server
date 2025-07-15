package com.devoops.event;

import org.springframework.context.ApplicationEvent;

public class AnaylzeMyPrEvent extends ApplicationEvent {

    public AnaylzeMyPrEvent(Object source) {
        super(source);
    }
}
