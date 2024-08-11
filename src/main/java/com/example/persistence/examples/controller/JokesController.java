package com.example.persistence.examples.controller;

import com.example.persistence.examples.model.db.Author;
import com.example.persistence.examples.model.db.JokeView;
import com.example.persistence.examples.model.domain.JokeModel;
import com.example.persistence.examples.model.dto.JokeSaveRequest;
import com.example.persistence.examples.service.AuthorsService;
import com.example.persistence.examples.service.JokesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JokesController {

    private static final String JOKE_SAVED = "Joke was successfully saved";

    private final JokesService jokesService;
    private final AuthorsService authorsService;

    @Autowired
    public JokesController(JokesService jokesService, AuthorsService authorsService) {
        this.jokesService = jokesService;
        this.authorsService = authorsService;
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

    @GetMapping("/jokes/db/punchline")
    public ResponseEntity<List<JokeModel>> findByPunchlineStartsWith(@RequestParam String word) {
        return ResponseEntity.ok(jokesService.findByPunchlineStartsWith(word));
    }

    @GetMapping("/jokes/db/type")
    public ResponseEntity<List<JokeView>> getJokeByType(@RequestParam String type) {
        return ResponseEntity.ok(jokesService.getJokeByType(type));
    }

    @PutMapping("/jokes/db/update/type")
    public ResponseEntity<String> updateJokesType(@RequestParam String type) {
        jokesService.updateJokesType(type);
        return ResponseEntity.ok("Jokes type updated");
    }

    @PostMapping("/db/author")
    public ResponseEntity<String> addJokesAuthor(@RequestParam String author, @RequestParam Long id) {
        jokesService.updateJokeAuthor(author, id);
        return ResponseEntity.ok("Joke author added");
    }

    @GetMapping("/authors")
    public ResponseEntity<List<Author>> getAuthors() {
        List<Author> list = authorsService.getAuthors();
        return ResponseEntity.ok(list);
    }
}
