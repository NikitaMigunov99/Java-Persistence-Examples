package com.example.persistence.examples.listener;

import org.springframework.context.ApplicationEvent;

public class EntityEvent extends ApplicationEvent {

    private final AccessType accessType;

    public EntityEvent(Object source, AccessType accessType) {
        super(source);
        this.accessType = accessType;
    }

    public AccessType getAccessType() {
        return accessType;
    }
}
