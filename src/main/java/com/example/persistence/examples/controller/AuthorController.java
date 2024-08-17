package com.example.persistence.examples.controller;

import com.example.persistence.examples.model.db.AuthorView;
import com.example.persistence.examples.model.domain.Author;
import com.example.persistence.examples.service.AuthorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthorController {

    private final AuthorsService authorsService;

    @Autowired
    public AuthorController(AuthorsService authorsService) {
        this.authorsService = authorsService;
    }

    @PostMapping("/author")
    public ResponseEntity<String> addJokesAuthor(@RequestParam String author, @RequestParam Long id) {
        authorsService.createJokeAuthor(author, id);
        return ResponseEntity.ok("Joke author added");
    }

    @GetMapping("/author/name")
    public ResponseEntity<List<AuthorView>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(authorsService.findByName(name));
    }

    @PutMapping("/author")
    public ResponseEntity<String> addJokeToAuthor(@RequestParam Long authorId, @RequestParam Long id) {
        authorsService.addJokeToAuthor(authorId, id);
        return ResponseEntity.ok("Joke author added");
    }

    @GetMapping("/authors")
    public ResponseEntity<List<Author>> getAuthors() {
        List<Author> list = authorsService.getAuthors();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/authors/delete")
    public ResponseEntity<String> deleteAuthor(@RequestParam String author) {
        authorsService.deleteAuthor(author);
        return ResponseEntity.ok("Joke author deleted");
    }
}
