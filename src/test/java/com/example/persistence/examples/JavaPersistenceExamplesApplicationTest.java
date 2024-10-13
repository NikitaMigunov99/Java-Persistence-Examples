package com.example.persistence.examples;

import com.example.persistence.examples.model.db.JokeEntity;
import com.example.persistence.examples.repository.JokesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class JavaPersistenceExamplesApplicationTest {

    private static final String DATABASE_NAME = "jokes-app";

    @Autowired
    private JokesRepository jokesRepository;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
            .withDatabaseName(DATABASE_NAME).withUsername("username").withPassword("password");
            //.withInitScript("test-data.sql");

    static {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    @Order(value = 1)
    void testConnectionToDatabase() {
        Assertions.assertNotNull(jokesRepository);
    }

    @Test
    @Order(value = 2)
    void testAddJoke() {
        Assertions.assertEquals(0, jokesRepository.findAll().size());

        jokesRepository.save(new JokeEntity("general", "Is there a hole in your shoe?", "No… Then how’d you get your foot in it?"));
        jokesRepository.save(new JokeEntity("general", "What do vegetarian zombies eat?", "Grrrrrainnnnnssss."));
        jokesRepository.save(new JokeEntity("general", "Did you hear about the cheese factory that exploded in France?", "There was nothing left but de Brie."));

        Assertions.assertEquals(3, jokesRepository.findAll().size());
    }

}
