package com.example.persistence.examples.model.dto;

/**
 * Request to safe a joke in database
 *
 * @param type      joke's category
 * @param setup     setup
 * @param punchline punchline
 */
public record JokeSaveRequest(
        String type,
        String setup,
        String punchline) {
}
