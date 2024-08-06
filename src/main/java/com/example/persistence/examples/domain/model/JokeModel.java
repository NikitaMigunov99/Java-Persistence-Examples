package com.example.persistence.examples.domain.model;

public record JokeModel(
        String id,
        String type,
        String setup,
        String punchline) {
}