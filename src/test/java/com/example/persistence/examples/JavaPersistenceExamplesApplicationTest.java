package com.example.persistence.examples;

import com.example.persistence.examples.model.db.JokeEntity;
import com.example.persistence.examples.model.domain.JokeModel;
import com.example.persistence.examples.model.dto.JokeSaveRequest;
import com.example.persistence.examples.repository.JokesRepository;
import com.example.persistence.examples.service.JokesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootTest
@Testcontainers
@Profile("local")
public class JavaPersistenceExamplesApplicationTest {

    private static final Set<Integer> redisClusterPorts = Set.of(7000, 7001, 7002, 7003, 7004, 7005);
    private static final String DATABASE_NAME = "jokes-app";
    private static final long ID = 2L;

    @Autowired
    private JokesRepository jokesRepository;

    @Autowired
    private JokesService jokesService;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
            .withDatabaseName(DATABASE_NAME).withUsername("username").withPassword("password");
    private static final GenericContainer<?> redis = new GenericContainer<>(
            DockerImageName.parse("grokzen/redis-cluster:6.0.7"))
            .withExposedPorts(redisClusterPorts.toArray(new Integer[0]));

    static {
        postgreSQLContainer.start();
        //redis.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);

//        List<String> nodes = new ArrayList<>();
//        String hostAddress = redis.getHost();
//        redisClusterPorts.forEach(port -> {
//            Integer mappedPort = redis.getMappedPort(port);
//            nodes.add(hostAddress + ":" + mappedPort);
//        });
//        System.out.println("debug nodes: " + nodes);
//        dynamicPropertyRegistry.add("redis.config.nodes", () -> nodes);
    }

    @AfterEach
    public void tearDown() {
        jokesService.removeAll();
    }

//    @Test
//    @Order(value = 1)
    void testConnectionToDatabase() {
        Assertions.assertNotNull(jokesRepository);
    }

//    @Test
//    @Order(value = 2)
    void testAddJoke() {
        Assertions.assertEquals(0, jokesRepository.findAll().size());

        jokesRepository.save(new JokeEntity("general", "Is there a hole in your shoe?", "No… Then how’d you get your foot in it?"));
        jokesRepository.save(new JokeEntity("general", "What do vegetarian zombies eat?", "Grrrrrainnnnnssss."));
        jokesRepository.save(new JokeEntity("general", "Did you hear about the cheese factory that exploded in France?", "There was nothing left but de Brie."));

        Assertions.assertEquals(3, jokesRepository.findAll().size());
    }

//    @Test
//    @DisplayName("save jokes")
    public void saveJoke() {
        JokeSaveRequest firstRequest = new JokeSaveRequest("programming", "What do you get when you cross a React developer with a mathematician?", "A function component.");
        JokeSaveRequest secondRequest = new JokeSaveRequest("general", "What time is it?", "I don't know... it keeps changing.");

        jokesService.saveJoke(firstRequest);

        var firstJoke = new JokeModel(1L, "programming", "What do you get when you cross a React developer with a mathematician?", "A function component.");
        System.out.println("\nget joke");
        Assertions.assertEquals(List.of(firstJoke), jokesService.getJokesDB());

        System.out.println("\nget joke");
        Assertions.assertEquals(List.of(firstJoke), jokesService.getJokesDB());

        jokesService.saveJoke(secondRequest);

        var secondJoke = new JokeModel(2L, "general", "What time is it?", "I don't know... it keeps changing.");
        Assertions.assertEquals(List.of(firstJoke, secondJoke), jokesService.getJokesDB());
    }

    @Test
    @DisplayName("update joke")
    public void updateJoke() {
        var firstRequest = new JokeSaveRequest("programming", "What do you get when you cross a React developer with a mathematician?", "A function component.");
        var secondRequest = new JokeSaveRequest("general", "What time is it?", "I don't know... it keeps changing.");

        jokesService.saveJoke(firstRequest);
        jokesService.saveJoke(secondRequest);

        var firstJoke = new JokeModel(1L, "programming", "What do you get when you cross a React developer with a mathematician?", "A function component.");
        var secondJoke = new JokeModel(2L, "general", "What time is it?", "I don't know... it keeps changing.");

        System.out.println("\nget jokes");
        List<JokeModel> models = jokesService.getJokesDB();
        Assertions.assertEquals(List.of(firstJoke, secondJoke), models);
        System.out.println("\nget jokes");
        Assertions.assertEquals(List.of(firstJoke, secondJoke), jokesService.getJokesDB());

        long realID = models.get((int) ID - 1).id();

        System.out.println("\nget joke id=" + realID);
        Assertions.assertEquals(secondJoke, jokesService.getJokeByIdDB(realID));
        System.out.println("\nget joke id=" + realID);
        Assertions.assertEquals(secondJoke, jokesService.getJokeByIdDB(realID));

        System.out.println("\nWithout Caching get joke id=" + realID);
        Assertions.assertEquals(secondJoke, jokesService.getJokeByIdDBWithoutCaching(realID));
        System.out.println("\nWithout Caching get joke id=" + realID);
        Assertions.assertEquals(secondJoke, jokesService.getJokeByIdDBWithoutCaching(realID));

        var updatedJokeRequest = new JokeSaveRequest("general", "What did the duck say when he bought lipstick?", "Put it on my bill");
        var updatedJoke = new JokeModel(2L, "general", "What did the duck say when he bought lipstick?", "Put it on my bill");
        jokesService.updateJoke(realID, updatedJokeRequest);

        System.out.println("\nget jokes");
        Assertions.assertEquals(List.of(firstJoke, updatedJoke), jokesService.getJokesDB());
        System.out.println("\nget jokes");
        Assertions.assertEquals(List.of(firstJoke, updatedJoke), jokesService.getJokesDB());

        System.out.println("\nget joke id=" + realID);
        Assertions.assertEquals(updatedJoke, jokesService.getJokeByIdDB(realID));
        System.out.println("\nget joke id=" + realID);
        Assertions.assertEquals(updatedJoke, jokesService.getJokeByIdDB(realID));
    }

//    @Test
//    @DisplayName("remove all jokes")
    public void removeAllJokes() {
        var firstRequest = new JokeSaveRequest("programming", "What do you get when you cross a React developer with a mathematician?", "A function component.");
        var secondRequest = new JokeSaveRequest("general", "What time is it?", "I don't know... it keeps changing.");

        jokesService.saveJoke(firstRequest);
        jokesService.saveJoke(secondRequest);

        var firstJoke = new JokeModel(1L, "programming", "What do you get when you cross a React developer with a mathematician?", "A function component.");
        var secondJoke = new JokeModel(2L, "general", "What time is it?", "I don't know... it keeps changing.");

        System.out.println("\nget jokes");
        List<JokeModel> models = jokesService.getJokesDB();
        Assertions.assertEquals(List.of(firstJoke, secondJoke), models);
        System.out.println("\nget jokes");
        Assertions.assertEquals(List.of(firstJoke, secondJoke), jokesService.getJokesDB());

        long realID = models.get((int) ID - 1).id();

        System.out.println("\nget joke id=" + realID);
        Assertions.assertEquals(secondJoke, jokesService.getJokeByIdDB(realID));
        System.out.println("\nget joke id=" + realID);
        Assertions.assertEquals(secondJoke, jokesService.getJokeByIdDB(realID));

        System.out.println("\nremove all");
        jokesService.removeAll();

        System.out.println("\nget jokes");
        Assertions.assertEquals(new ArrayList<>(), jokesService.getJokesDB());

        Assertions.assertThrows(Exception.class, () -> jokesService.getJokeByIdDB(realID));
    }

//    @Test
//    @DisplayName("remove joke by id")
    public void removeJokesById() {
        var firstRequest = new JokeSaveRequest("programming", "What do you get when you cross a React developer with a mathematician?", "A function component.");
        var secondRequest = new JokeSaveRequest("general", "What time is it?", "I don't know... it keeps changing.");

        jokesService.saveJoke(firstRequest);
        jokesService.saveJoke(secondRequest);

        var firstJoke = new JokeModel(1L, "programming", "What do you get when you cross a React developer with a mathematician?", "A function component.");
        var secondJoke = new JokeModel(2L, "general", "What time is it?", "I don't know... it keeps changing.");

        System.out.println("\nget jokes");
        List<JokeModel> models = jokesService.getJokesDB();
        Assertions.assertEquals(List.of(firstJoke, secondJoke), models);
        System.out.println("\nget jokes");
        Assertions.assertEquals(List.of(firstJoke, secondJoke), jokesService.getJokesDB());

        long realID = models.get((int) ID - 1).id();

        System.out.println("\nget joke id=" + realID);
        Assertions.assertEquals(secondJoke, jokesService.getJokeByIdDB(realID));
        System.out.println("\nget joke id=" + realID);
        Assertions.assertEquals(secondJoke, jokesService.getJokeByIdDB(realID));

        System.out.println("\nremove joke id=" + realID);
        jokesService.removeJokeById(realID);

        System.out.println("\nget jokes");
        Assertions.assertEquals(List.of(firstJoke), jokesService.getJokesDB());

        Assertions.assertThrows(Exception.class, () -> jokesService.getJokeByIdDB(realID));
    }

}
