package com.example.persistence.examples.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EntityListener {

    @EventListener
    public void acceptEvent(EntityEvent entityEvent) {
        System.out.println("New event with type " + entityEvent.getAccessType());
    }
}
