package com.example.persistence.examples.repository;

import com.example.persistence.examples.model.db.JokeEntity;
import com.example.persistence.examples.model.db.JokeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository for Jokes
 */
public interface JokesRepository extends JpaRepository<JokeEntity, Long> {

    @Query("select count(j) from JokeEntity j")
    Integer getJokesCount();

    List<JokeEntity> findByPunchlineStartsWithIgnoreCase(String word);

    List<JokeView> getJokeByType(String type);

    @Modifying
    @Query("update JokeEntity j set j.type = 'general' where j.type = :type")
    @Transactional
    void updateJokesType(@Param("type") String type);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE joke", nativeQuery = true)
    void truncateTable();
}
