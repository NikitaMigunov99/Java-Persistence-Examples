package com.example.persistence.examples.service;

import com.example.persistence.examples.model.db.Author;
import com.example.persistence.examples.repository.AuthorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorsService {

    private final AuthorsRepository repository;

    @Autowired
    public AuthorsService(AuthorsRepository repository) {
        this.repository = repository;
    }

    public List<Author> getAuthors() {
        return repository.findAll();
    }
}
