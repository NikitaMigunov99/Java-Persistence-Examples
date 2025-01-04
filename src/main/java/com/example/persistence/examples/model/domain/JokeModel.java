package com.example.persistence.examples.model.domain;

import java.util.Objects;

/**
 * Model of joke
 */
public record JokeModel(
        Long id,
        String type,
        String setup,
        String punchline) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JokeModel jokeModel)) return false;
        return type.equals(jokeModel.type) && setup.equals(jokeModel.setup) && punchline.equals(jokeModel.punchline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, setup, punchline);
    }

    @Override
    public String toString() {
        return "JokeModel{" +
                "type='" + type + '\'' +
                ", setup='" + setup + '\'' +
                ", punchline='" + punchline + '\'' +
                '}';
    }
}