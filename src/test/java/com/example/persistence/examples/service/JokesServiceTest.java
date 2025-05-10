package com.example.persistence.examples.service;

import com.example.persistence.examples.model.domain.JokeModel;
import com.example.persistence.examples.model.dto.JokeSaveRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@DirtiesContext
public class JokesServiceTest {

    private static final long ID = 2L;

    @Autowired
    private JokesService jokesService;

    @AfterEach
    public void tearDown() {
        jokesService.removeAll();
    }

    @Test
    @DisplayName("save jokes")
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

    @Test
    @DisplayName("remove all jokes")
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

    @Test
    @DisplayName("remove joke by id")
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
