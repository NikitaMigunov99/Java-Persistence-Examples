package com.example.persistence.examples.controller;

import com.example.persistence.examples.model.domain.JokeModel;
import com.example.persistence.examples.model.dto.JokeSaveRequest;
import com.example.persistence.examples.service.JokesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JokesController {

    private static final String JOKE_SAVED = "Joke was successfully saved";

    private final JokesService jokesService;

    @Autowired
    public JokesController(JokesService jokesService) {
        this.jokesService = jokesService;
    }

    @GetMapping("/joke")
    public ResponseEntity<JokeModel> getJoke() {
        JokeModel joke = jokesService.getJoke();
        return ResponseEntity.ok(joke);
    }

    @GetMapping("/joke/{id}")
    public ResponseEntity<JokeModel> getJokeById(@PathVariable String id) {
        JokeModel joke = jokesService.getJokeById(id);
        return ResponseEntity.ok(joke);
    }

    @GetMapping("/jokes")
    public ResponseEntity<List<JokeModel>> getJokes() {
        List<JokeModel> list = jokesService.getJokesList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/jokes/db")
    public ResponseEntity<List<JokeModel>> getJokesDB() {
        List<JokeModel> list = jokesService.getJokesDB();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/save/joke")
    public ResponseEntity<String> saveJoke(@RequestBody JokeSaveRequest request) {
        jokesService.saveJoke(request);
        return ResponseEntity.ok(JOKE_SAVED);
    }

    @PostMapping("/save/joke/{id}")
    public ResponseEntity<String> saveJokeById(@PathVariable String id) {
        jokesService.saveJokeById(id);
        return ResponseEntity.ok(JOKE_SAVED);
    }

    @GetMapping("/jokes/db/count")
    public ResponseEntity<String> getJokesCount() {
        int count = jokesService.getJokesCount();
        return ResponseEntity.ok("Jokes count is " + count);
    }
}
