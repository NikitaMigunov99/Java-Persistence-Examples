package com.example.persistence.examples.repository;

import com.example.persistence.examples.model.db.JokeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Jokes
 */
public interface JokesRepository extends JpaRepository<JokeEntity, Long> {
}
