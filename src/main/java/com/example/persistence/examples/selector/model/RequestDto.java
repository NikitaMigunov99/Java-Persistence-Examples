package com.example.persistence.examples.selector.model;

public class RequestDto {
    private final String type;

    public RequestDto(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
