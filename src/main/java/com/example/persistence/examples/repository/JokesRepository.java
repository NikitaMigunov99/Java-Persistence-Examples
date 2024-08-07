package com.example.persistence.examples.repository;

import com.example.persistence.examples.model.db.JokeEntity;
import com.example.persistence.examples.model.db.JokeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for Jokes
 */
public interface JokesRepository extends JpaRepository<JokeEntity, Long> {

    @Query("select count(j) from JokeEntity j")
    Integer getJokesCount();

    List<JokeEntity> findByPunchlineStartsWithIgnoreCase(String word);

    List<JokeView> getJokeByType(String type);
}
