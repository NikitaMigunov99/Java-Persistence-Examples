package com.example.persistence.examples.controller;

import com.example.persistence.examples.domain.model.JokeModel;
import com.example.persistence.examples.domain.service.JokesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JokesController {

    private final JokesService jokesService;

    @Autowired
    public JokesController(JokesService jokesService) {
        this.jokesService = jokesService;
    }

    @GetMapping("/joke")
    public ResponseEntity<JokeModel> getJoke()  {
        JokeModel joke = jokesService.getJoke();
        return ResponseEntity.ok(joke);
    }

    @GetMapping("/joke/{id}")
    public ResponseEntity<JokeModel> getJokeById(@PathVariable String id)  {
        JokeModel joke = jokesService.getJokeById(id);
        return ResponseEntity.ok(joke);
    }

    @GetMapping("/jokes")
    public ResponseEntity<List<JokeModel>> getJokes() {
        List<JokeModel> list = jokesService.getJokesList();
        return ResponseEntity.ok(list);
    }
}
