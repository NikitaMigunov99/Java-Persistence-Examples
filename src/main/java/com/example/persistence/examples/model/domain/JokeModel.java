package com.example.persistence.examples.model.domain;

/**
 * Model of joke
 */
public record JokeModel(
        String id,
        String type,
        String setup,
        String punchline) {
}