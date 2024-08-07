package com.example.persistence.examples.model.db;

/**
 * Projection of {@link JokeEntity}
 * @see <a href="https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html">Projections</a>
 */
public interface JokeView {

    String getPunchline();
}
