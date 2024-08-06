package com.example.persistence.examples.model.dto;

/**
 * Request to safe a joke in database
 */
public record JokeSaveRequest(
        String type,
        String setup,
        String punchline) {
}
